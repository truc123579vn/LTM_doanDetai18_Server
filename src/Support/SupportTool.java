package Support;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SupportTool {
    private static ObjectInputStream inserver = null;
    private static ObjectOutputStream outserver = null;

    public static ObjectInputStream getInputStream ()
    {
        return inserver;
    }
    public static ObjectOutputStream getOutputStream ()
    {
        return outserver;
    }

    public static void InitializeInputStream(Socket s) throws IOException {

            inserver=new ObjectInputStream(s.getInputStream());

    }

    public static void InitializeOutputStream(Socket s) throws IOException {

            outserver=new ObjectOutputStream(s.getOutputStream());
    }
}
