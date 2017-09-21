package br.com.smartstudyplan.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import br.com.smartstudyplan.R;
import br.com.smartstudyplan.view.CustomLinearLayout;

/**
 * Está classe é responsável por gerar a imagem que vai ser compartilhado a partir de uma
 * <code>View</code> e salvá-la temporariamente na memória.
 */
public class ShareImageUtil {
    private static final String TAG = "ShareImageUtil";

    /**
     * Gera uma imagem a partir de uma <code>View</code>.
     *
     * @param context o contexto de onde o método é chamado
     * @param mLayout <code>View</code> que será utilizado para gerar a imagem
     * @return <code>Bitmap</code> com a imagem gerada
     */
    public static Bitmap getBitmapByCustomView( Context context, CustomLinearLayout mLayout ){
        if( mLayout == null ){
            return null;
        }

        DisplayMetrics dm = context.getResources().getDisplayMetrics();

        Bitmap returnedBitmap = Bitmap.createBitmap(
                mLayout.getWidth(),
                mLayout.getChildAt(0).getHeight(),
                Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE);
        mLayout.paintDraw(canvas);

        Bitmap appIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
        canvas.drawBitmap(appIcon, (int)(10 * dm.density), (int)(10 * dm.density), new Paint() );

        appIcon.recycle();

        return returnedBitmap;
    }

    /**
     * Salva a imagem na memória e retorna um arquivo.
     *
     * @param context o contexto de onde o método é chamado
     * @param bitmap a imagem a ser salva
     * @param bitmapName o nome do arquivo a ser gerado
     * @return o arquivo salvo em memória
     */
    public static File saveBitmapOnExternalStorage( Context context, Bitmap bitmap, String bitmapName ){
        if( bitmap == null ){
            return null;
        }

        File file = null;

        File path = context.getExternalFilesDir(null);

        if( path != null ){
            file = createFile(path.getAbsolutePath(), bitmapName + ".png");

            if( file != null ){
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream( file );
                    bitmap.compress( Bitmap.CompressFormat.PNG, 100, fos );
                    SLog.d( TAG, "File path: " + file.getAbsolutePath() );
                }
                catch (FileNotFoundException e) {
                    Log.e(TAG, "File not found.", e);
                }
                finally{
                    if( fos != null ){
                        try {
                            fos.close();
                        } catch (IOException e) {
                            Log.e( TAG, "Error on try save image", e );
                        }
                    }
                }
            }
        }

        bitmap.recycle();

        return file;
    }

    /**
     * Cria um arquivo.
     *
     * @param dirPath o caminho do arquivo
     * @param fileName o nome do arquivo
     * @return o arquivo gerado
     */
    private static File createFile( String dirPath, String fileName ) {
        File result = null;

        SLog.d( TAG, "DBG - Saving file [" + fileName + "] on dir: [" + dirPath + "]" );

        File storageDir = createDir( dirPath );
        if ( storageDir == null ) {
            Log.e( TAG, "DBG - Could not create the dir on external media." );
            return result;
        }

        File file = new File( storageDir, fileName );
        try {
            if ( file.exists() || file.createNewFile() ) {
                result = file;
            }
            else {
                Log.e( TAG, "DBG - Could not create the file on external media." );
            }
        }
        catch ( IOException ioe ) {
            Log.e( TAG, "DBG - Could not create the file that will store content data.", ioe );
        }
        return result;
    }

    /**
     * Cria um diretório.
     *
     * @param dirPath o caminho do diretório
     * @return o diretório
     */
    private static File createDir( String dirPath ) {
        boolean isDirAvailable = false;
        File dir = new File( dirPath );

        isDirAvailable = dir.exists() ? true : dir.mkdirs();

        return ( ( isDirAvailable ) ? dir : null );
    }
}
