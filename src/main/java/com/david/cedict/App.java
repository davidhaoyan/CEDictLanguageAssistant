package com.david.cedict;
import com.david.cedict.db.EmbeddedH2;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;


public class App 
{
    public static void main( String[] args )
    {
        if (args[0].equals("setup")) {
            EmbeddedH2 h2 = new EmbeddedH2();
            h2.createDB();
            h2.createVSM();
        } else if (args[0].equals("run")) {
            Retriever r = new Retriever();
            try {
                GlobalScreen.registerNativeHook();
            } catch (NativeHookException e) {
                System.err.println("There was a problem registering the native hook.");
                System.err.println(e.getMessage());
                System.exit(1);
            }
            GlobalScreen.addNativeKeyListener(new GUI(r));
        } else {
            System.out.println("ARG ERROR");
        }
    }
}
