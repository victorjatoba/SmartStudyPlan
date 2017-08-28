package br.com.smartstudyplan.util;

import android.util.Log;

import br.com.smartstudyplan.BuildConfig;

/**
 * Utilitário responsável por exibir o Log, caso esteja habilitado.
 */
public class SLog {
    private static final boolean ACTIVE = BuildConfig.DEBUG;

    /**
     * Mostra uma mensagem de log de debug.
     *
     * @param tag o label do log
     * @param message o texto do log
     */
    public static void d(String tag, String message){
        if( ACTIVE ){
            Log.d(tag, message);
        }
    }

    /**
     * Mostra uma mensagem de log de erro.
     *
     * @param tag o label do log
     * @param message o texto do log
     */
    public static void e(String tag, String message){
        if( ACTIVE ){
            Log.e(tag, message);
        }
    }

    /**
     * Mostra uma mensagem de log de alerta.
     *
     * @param tag o label do log
     * @param message o texto do log
     */
    public static void w(String tag, String message){
        if( ACTIVE ){
            Log.w(tag, message);
        }
    }
}
