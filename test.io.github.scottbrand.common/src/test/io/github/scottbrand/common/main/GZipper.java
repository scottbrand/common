package test.io.github.scottbrand.common.main;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;



public class GZipper
{

    // @Override
    public <T> byte[] toBytes(T obj) throws IOException
    {
        if (obj != null)
        {
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(bos))
            {
                oos.writeObject(obj);
                oos.flush();
                return bos.toByteArray();
            }
        }
        else
        {
            return null;
        }
    }





    public <T> byte[] toBytesCompressed(T obj) throws IOException
    {
        if (obj != null)
        {
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream(32_000); GZIPOutputStream zos = new GZIPOutputStream(bos); ObjectOutputStream oos = new ObjectOutputStream(zos))
            {
                oos.writeObject(obj);
                oos.flush();
                zos.finish();
                return bos.toByteArray();
            }
        }
        else
        {
            return null;
        }
    }





    @SuppressWarnings("unchecked")
    public <T> T fromBytes(byte[] obj) throws IOException, ClassNotFoundException
    {
        if (obj != null)
        {
            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(obj)))
            {
                return (T) ois.readObject();
            }
        }
        else
        {
            return null;
        }
    }





    @SuppressWarnings("unchecked")
    public <T> T fromCompressedBytes(byte[] obj) throws IOException, ClassNotFoundException
    {
        if (obj != null)
        {
            try (ObjectInputStream ois = new ObjectInputStream(new GZIPInputStream(new ByteArrayInputStream(obj))))
            {
                return (T) ois.readObject();
            }
        }
        else
        {
            return null;
        }
    }





    public void write(Object data, OutputStream os)
    {
        try
        {
            GZIPOutputStream gzipOut = new GZIPOutputStream(os);
            ObjectOutputStream objectOut = new ObjectOutputStream(gzipOut);
            objectOut.writeObject(data);
            objectOut.flush();
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
    }





    public Object read(InputStream is)
    {
        try
        {
            GZIPInputStream gzipIn = new GZIPInputStream(is);
            ObjectInputStream objectIn = new ObjectInputStream(gzipIn);
            return objectIn.readObject();
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            return null;
        }
    }





    public static void main(String[] args)
    {
        GZipper gz = new GZipper();
        try
        {
            Foo foo1 = new Foo();
            byte[] o = gz.toBytes(foo1);
            System.out.println("o size=" + o.length);
            System.out.println("String of o " + new String(o));

            Foo foo2 = gz.fromBytes(o);
            System.out.println("foo1 = " + foo1 + ", foo2 = " + foo2);
            
            Thread.sleep(10);
            Foo foo3 = new Foo();
            byte[] oc = gz.toBytesCompressed(foo3);
            System.out.println("oc size=" + oc.length);
            System.out.println("String of oc " + new String(oc));
            
            Foo foo4 = gz.fromCompressedBytes(oc);
            System.out.println("foo3 = " + foo3 + ", foo4 = " + foo4);
            
            // ByteArrayOutputStream os = new ByteArrayOutputStream(32_000);
            // System.out.println("os size=" + os.size());
            // gz.write(new Foo(), os);
            // System.out.println("os size=" + os.size());
            // Object o = gz.read(new ByteArrayInputStream(os.toByteArray()));
            // System.out.println("o is: " + o);
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
    }
}
