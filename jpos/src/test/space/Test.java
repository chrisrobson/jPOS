    package space;

    import junit.framework.*;
    import java.io.*;
    import java.util.*;
    import org.jpos.space.*;

    public class Test extends TestCase implements SpaceListener {
        Space sp;
        String rx;

        public Test (String name) {
            super (name);
            sp = new TransientSpace ();
        }

        public void testSimpleOut() throws Exception {
            Object o = new Boolean (true);

            sp.out ("Key1", o);
            Object o1 = sp.in ("Key1");

            assertTrue (o.equals (o1));
        }
        public void notify (Object key, Object value) {
            rx = (String) sp.in (key);
        }

        public void testLeasedReference () throws Exception {
            Object o = new Boolean (true);

            sp.out ("Key1", new LeasedReference (o, 100));
            Object o1 = sp.in ("Key1");
            assertTrue (o.equals (o1));

            sp.out ("Key1", new LeasedReference (o, 100));
            o1 = sp.rdp ("Key1");
            assertTrue (o1 != null);
            Thread.sleep (50);
            o1 = sp.rdp ("Key1");
            assertTrue (o1 != null);
            Thread.sleep (200);
            o1 = sp.rdp ("Key1");
            assertTrue (o1 == null);
        }
        public void testListener () throws Exception {
            sp.addListener ("Key2", this);
            sp.out ("Key2", "Test");
            assertTrue (sp.inp ("Key2") == null);
            assertTrue ("Test".equals (rx));
        }
        public void testDefaultSpace () throws Exception {
            Space sp = TransientSpace.getSpace ();
            assertTrue (sp != null);
            TransientSpace.getSpace ("OtherSpace");
            TransientSpace.getSpace ("OtherSpace");
            Object obj = sp.rdp ("jpos:space/OtherSpace");
            assertTrue (obj != null);
            assertTrue (obj instanceof TransientSpace);
        }
    }

