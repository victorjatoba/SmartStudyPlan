package br.com.smartstudyplan.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import br.com.smartstudyplan.R;
import br.com.smartstudyplan.adapter.SubjectAdapter;
import br.com.smartstudyplan.bean.Step;
import br.com.smartstudyplan.bean.Subject;
import br.com.smartstudyplan.dialog.SubjectAddDialog;
import br.com.smartstudyplan.dialog.SubjectAddListener;
import br.com.smartstudyplan.manager.StudyPlanManager;

/**
 * Esta classe contém a lista de matérias a serem estudadas. A partir dela, o usuário
 * poderá adicionar novas matérias à lista, editar as matérias já adicionadas e removê-las
 * da lista.
 */
@SuppressLint("Registered")
@EActivity(R.layout.activity_subject)
public class SubjectActivity extends AppCompatActivity {

    /**
     * Determina o máximo de matérias que podem ser adicionados.
     */
    private static final int MAX_SUBJECTS = 12;

    @ViewById ListView  subjectList;
    @ViewById View      empty;
    @ViewById Button    subjectContinueButton;

    @Bean SubjectAdapter   adapter;
    @Bean StudyPlanManager manager;

    private List<Subject> mSubjects;

    /**
     * Primeiro método chamado ao ser iniciada a Activity.
     */
    @AfterViews
    void init(){
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_actionbar));

        manager.saveStep( Step.STEP_SUBJECT );

        subjectList.setEmptyView(empty);
        subjectList.setAdapter(adapter);

        subjectList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        loadSubjects();
    }

    /**
     * Cria o menu que será mostrado na Action Bar
     *
     * @param menu o <code>Menu</code> a ser criado
     * @return deve retornar <code>true</code> para o menu ser exibido, <code>false</code> fará com
     * que o menu não seja exibido
     */
    @SuppressLint("AlwaysShowAction")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Add menu item manually to resolve the show always error on support package
        MenuItem item = menu.add(Menu.NONE, R.id.add_subject, 1, R.string.subject_add_menu);
        item.setIcon(R.drawable.ic_action_add);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Carrega a lista de matérias e solicita a sua atualização na tela.
     */
    @Background
    void loadSubjects(){
        mSubjects = manager.getSubjects();
        updateLayout(mSubjects);
    }

    /**
     * Faz a lista de matérias aparecer na tela.
     *
     * @param subjects a lista de matérias a ser mostrada na tela.
     */
    @UiThread
    void updateLayout( List<Subject> subjects ){
        adapter.setContent( subjects );
    }

    /**
     * Chama o dialog de adicionar nova matéria ao se clicar na respectiva opção do menu.
     */
    @OptionsItem
    void addSubject(){
        if(mSubjects.size() < MAX_SUBJECTS) {
            showDialog(new Subject(0, "", 0, 0, 0), R.string.subject_add);
        } else {
            Toast.makeText(this, getString(R.string.add_max_subject_number, MAX_SUBJECTS), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Chama o dialog de adicionar nova matéria ao se clicar no centro da tela caso a lista esteja vazia.
     */
    @Click
    void empty(){
        addSubject();
    }

    /**
     * Chama a próxima tela do aplicativo.
     */
    @Click
    void subjectContinueButton(){
        if(mSubjects.isEmpty()) {
            Toast.makeText(this, R.string.empty_subject_list, Toast.LENGTH_LONG).show();
        } else {
            AvailabilityActivity_.intent( SubjectActivity.this ).start();
            finish();
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_left);
        }
    }

    /**
     * Chama a tela de ajuda (dicas).
     */
    @Click
    void subjectHelpButton(){
        HelpActivity_.intent( SubjectActivity.this )
                .title(getString(R.string.help_subject_title))
                .text(getString(R.string.help_subject_text))
                .start();
    }

    /**
     * Chama o Contextual Action Bar para realizar uma ação com uma matéria que foi selecionada.
     *
     * @param subject a matéria que foi selecionada
     */
    @ItemClick
    void subjectListItemClicked( Subject subject ){
        ActionMode mActionMode = startSupportActionMode( mActionModeCallBack );
        if (mActionMode != null) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
            mActionMode.setTitle(R.string.subject_edit);
            mActionMode.setTag(subject);
        }

        adapter.setSelectedSubject( subject );
    }

    /**
     * Mostra o dialog com as informações da matéria que foi selecionada ou com uma nova matéria.
     *
     * @param subject a matéria que foi selecionada para edição ou uma nova matéria
     * @param titleResource o recurso do título do dialog
     */
    private void showDialog(Subject subject, int titleResource){
        SubjectAddDialog.show(SubjectActivity.this, titleResource, subject, new SubjectAddListener() {
            @Override
            public void returnValues(Subject subject) {
                manager.addSubject(subject);
                loadSubjects();
            }
        });
    }

    /**
     * Contextual Action Bar utilizado quando seleciona uma matéria.
     */
    private ActionMode.Callback mActionModeCallBack = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater inflater = actionMode.getMenuInflater();
            inflater.inflate(R.menu.menu_edit_subject, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.edit_subject:
                    showDialog((Subject) actionMode.getTag(), R.string.subject_edit);
                    loadSubjects();
                    actionMode.finish();
                    break;
                case R.id.delete_subject:
                    manager.removeSubject((Subject) actionMode.getTag());
                    loadSubjects();
                    actionMode.finish();
                    break;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            adapter.setSelectedSubject( null );

            new Handler().postDelayed(() -> {
                if (getSupportActionBar() != null) {
                    getSupportActionBar().show();
                }
            }, 300);
        }
    };

    /**
     * Verifica se uma matéria já está adicionada na lista, evitando adicionar matérias com mesmo
     * nome.
     *
     * @param name Nome da matéria a ser verificado
     * @param id Caso seja diferente de 0, permite a edição de uma matéria específca
     * @return <code>true</code> se a matéria já está na lista, <code>false</code> se não estiver
     */
    public boolean findSubjectByName(String name, long id){
        if( (TextUtils.isEmpty(name) == false) && (mSubjects.isEmpty() == false) ){
            for( Subject subject : mSubjects ){
                if( name.toLowerCase().equals(subject.getName().toLowerCase()) &&
                        ( (id == 0) || (subject.getId() != id) ) ){
                    return true;
                }
            }
        }
        return false;
    }
}
