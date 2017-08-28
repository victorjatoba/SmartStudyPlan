package br.com.smartstudyplan.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import br.com.smartstudyplan.R;
import br.com.smartstudyplan.activity.SubjectActivity;
import br.com.smartstudyplan.adapter.SubjectIconAdapter;
import br.com.smartstudyplan.bean.Subject;

/**
 * Esta classe é responsável por mostrar o dialog de matérias.
 */
public class SubjectAddDialog {
    private static Subject mSubject;

    /**
     * Mostra o dialog de matérias.
     *
     * @param activity activity de onde o dialog é chamado
     * @param titleResource recurso de string do título do dialog
     * @param subject matéria que terá os atributos mostrados no dialog
     * @param listener listener que irá tratar o resultado do que foi feito no dialog
     */
    public static void show( final Activity activity, int titleResource, final Subject subject, final SubjectAddListener listener ){
        mSubject  = subject;

        LayoutInflater inflater = activity.getLayoutInflater();

        final Resources res = activity.getResources();

        RelativeLayout layout = (RelativeLayout) inflater.inflate( R.layout.dialog_subject, null );

        final TextView titleView = (TextView) layout.findViewById( R.id.title );
        final EditText subjectView  = (EditText) layout.findViewById( R.id.edit_subject_name );
        final GridView gridView = (GridView) layout.findViewById(R.id.icon_grid);
        final SeekBar weightSeek = (SeekBar) layout.findViewById(R.id.weight_seek);
        final SeekBar difficultSeek = (SeekBar) layout.findViewById(R.id.difficult_seek);

        titleView.setText( titleResource );

        SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                if( i >= 0 && i < 20 ){
                    seekBar.setThumb(res.getDrawable(R.drawable.circle_difficult_1));
                } else if( i >= 20 && i < 40 ){
                    seekBar.setThumb(res.getDrawable(R.drawable.circle_difficult_2));
                } else if ( i >= 40 && i < 60 ){
                    seekBar.setThumb(res.getDrawable(R.drawable.circle_difficult_3));
                } else if ( i >= 60 && i < 80){
                    seekBar.setThumb(res.getDrawable(R.drawable.circle_difficult_4));
                } else if ( i >= 80 && i <= 100 ){
                    seekBar.setThumb(res.getDrawable(R.drawable.circle_difficult_5));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { /* no code */ }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { /* no code */ }
        };

        weightSeek.setOnSeekBarChangeListener(seekBarChangeListener);
        difficultSeek.setOnSeekBarChangeListener(seekBarChangeListener);

        subjectView.setText(subject.getName());

        gridView.setAdapter(new SubjectIconAdapter(activity, new SubjectIconListener() {
            @Override
            public void setSelected(int value) {
                mSubject.setIcon(value);
            }

            @Override
            public int getSelected() {
                return mSubject.getIcon();
            }
        }));

        weightSeek.setProgress(subject.getWeight());
        difficultSeek.setProgress(subject.getDifficult());

        AlertDialog.Builder builder = new AlertDialog.Builder( activity );
//        builder.setTitle( mTitleResource );
        builder.setView(layout);

        builder.setPositiveButton( R.string.ok, null ); // Overriden abaixo
        builder.setNegativeButton( R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        });

        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(subjectView.getText().toString().replaceAll("\\s+", "").isEmpty()){
                            Toast.makeText(activity, R.string.subject_name_empty_messsage, Toast.LENGTH_SHORT).show();
                        }
                        else if(((SubjectActivity)activity).findSubjectByName(subjectView.getText().toString(), mSubject.getId())){
                            Toast.makeText(activity, R.string.subject_name_contain_subject_messsage, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            mSubject.setName(subjectView.getText().toString());
                            mSubject.setIcon(mSubject.getIcon());
                            mSubject.setWeight(weightSeek.getProgress());
                            mSubject.setDifficult(difficultSeek.getProgress());
                            if( listener != null ){
                                listener.returnValues(mSubject);
                            }

                            dialog.dismiss();
                        }
                    }
                });
            }
        });

        dialog.show();

        if( mSubject.getIcon() > 0 ){
            new Handler().postDelayed( new Runnable() {
                @Override
                public void run() {
                    gridView.smoothScrollToPosition(mSubject.getIcon());
                }
            }, 350 );
        }
    }
}
