package ixcode.platform.serialise;

import ixcode.platform.collection.Action;
import ixcode.platform.reflect.FieldReflector;
import ixcode.platform.reflect.ObjectBuilder;
import ixcode.platform.reflect.ObjectReflector;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;

import static ixcode.platform.io.IoStreamHandling.closeQuietly;
import static ixcode.platform.reflect.ObjectFactory.isClassAvailable;
import static ixcode.platform.reflect.ObjectFactory.loadClass;
import static ixcode.platform.reflect.ObjectReflector.reflect;
import static java.lang.String.format;

/**
 * Think the best thing to do is to first serialize the object to a map and then serialise the map into the byte array
 * as a single object
 */
public class ObjectSerialiser {


    public <T> T fromByteArray(byte[] bytes) {
        ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytes);

        try {
            final ObjectInputStream in = new ObjectInputStream(bytesIn);

            String className = (String) in.readObject();
            if (!isClassAvailable(className)) {
                throw new RuntimeException(format("Could not find class [%s] on the classpath", className));
            }

            Class<?> classToBuild = loadClass(className);

            ObjectReflector reflector = reflect(classToBuild);

            final ObjectBuilder builder = new ObjectBuilder(classToBuild);

            reflector.withEachNonTransientField(new Action<FieldReflector>() {
                @Override public void to(FieldReflector item, Collection<FieldReflector> tail) {
                    builder.setProperty(item.name).asObject(readObject(in));
                }
            });

            return builder.build();

        } catch (IOException e) {
            throw new RuntimeException(format("Could not read object from bytes (See Cause)"), e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(bytesIn);
        }

    }

    public byte[] toByteArray(final Object in) {
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();

        try {
            final ObjectOutputStream out = new ObjectOutputStream(bytesOut);

            out.writeObject(in.getClass().getName());

            ObjectReflector reflector = reflect(in.getClass());

            reflector.withEachNonTransientField(new Action<FieldReflector>() {
                @Override public void to(FieldReflector item, Collection<FieldReflector> tail) {
                    writeObject(out, item.valueFrom(in));
                }
            });

            return bytesOut.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(format("Could not serialize object [%s] (See Cause)", in.toString()), e);
        } finally {
            closeQuietly(bytesOut);
        }
    }

    private static final void writeObject(ObjectOutputStream out, Object object) {
        try {
            out.writeObject(object);
        } catch (IOException e) {
            throw new RuntimeException(format("Could not write object [%s] to stream (See cause)", object.toString()), e);
        }
    }

    private static Object readObject(ObjectInputStream in) {
        try {
            return in.readObject();
        } catch (IOException e) {
            throw new RuntimeException(format("Could not read object from stream (See cause)"), e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(format("Could not read object from stream (See cause)"), e);
        }
    }


}
