package tv.spielemeister.mpg.engine.net;

import tv.spielemeister.mpg.engine.world.Location;
import tv.spielemeister.mpg.engine.world.Vector;

import java.nio.charset.StandardCharsets;

public abstract class NetPacket {

    public byte packetType;

    public NetPacket(byte[] data){
        this.packetType = data[0];
        parse(data);
    }

    public NetPacket(byte packetType){
        this.packetType = packetType;
    }

    protected abstract void parse(byte[] data);

    public abstract byte[] encode(); // The first byte has to be the packetType

    public static void put(byte[] arr, byte[] val, int offset){
        if(offset+val.length <= arr.length)
            System.arraycopy(val, 0, arr, offset, val.length);
    }

    public static void put(byte[] arr, long val, int offset){
        if(offset + 7 < arr.length) {
            arr[offset] = (byte) (val >>> 56);
            arr[1+offset] = (byte) (val >>> 48);
            arr[2+offset] = (byte) (val >>> 40);
            arr[3+offset] = (byte) (val >>> 32);
            arr[4+offset] = (byte) (val >>> 24);
            arr[5+offset] = (byte) (val >>> 16);
            arr[6+offset] = (byte) (val >>> 8);
            arr[7+offset] = (byte) (val);
        }
    }


    public static void put(byte[] arr, char[] val, int offset){
        if(offset+val.length*2 <= arr.length)
            for(int i = 0; i < val.length; i++) {
                arr[i*2 + offset] = (byte) val[i];
                arr[i*2 + offset + 1] = (byte) (val[i] >>> 2);
            }
    }


    public static void put(byte[] arr, int val, int offset){
        if(offset + 4 <= arr.length) {
            arr[offset] = (byte) (val >>> 24);
            arr[1+offset] = (byte) (val >>> 16);
            arr[2+offset] = (byte) (val >>> 8);
            arr[3+offset] = (byte) (val);
        }
    }

    public static void put(byte[] arr, Vector v, int offset){
        put(arr, v.getX(), offset);
        put(arr, v.getY(), offset+4);
    }

    public static void put(byte[] arr, Location l, int offset){
        put(arr, (Vector) l, offset);
        put(arr, l.getRotation(), offset + 8);
        put(arr, l.getWorld(), offset + 12);
    }

    public static Vector getVector(byte[] arr, int offset){
        if(offset + 8 <= arr.length){
            return new Vector(getInteger(arr, offset), getInteger(arr, offset+4));
        }
        return null;
    }

    public static Location getLocation(byte[] arr, int offset){
        if(offset + 16 <= arr.length){
            Vector v = getVector(arr, offset);
            return new Location(getInteger(arr, offset + 12),
                    v.getX(), v.getY(), getInteger(arr, offset + 8));
        }
        return null;
    }

    public static byte[] get(byte[] arr, int offset, int len){
        byte[] ret = new byte[len];
        if(offset+len <= arr.length){
            System.arraycopy(arr, offset, ret, 0, len);
        }
        return ret;
    }

    public static char[] getCharArray(byte[] arr, int offset, int len){
        char[] ret = new char[len/2];
        if(offset+len <= arr.length){
            for(int i = 0; i < ret.length; i++){
                ret[i] = (char) ((arr[offset + i*2 + 1]<<2) | arr[offset + i*2]);
            }
        }
        return ret;
    }

    public static byte[] getRest(byte[] arr, int offset){
        return get(arr, offset, arr.length - offset);
    }

    public static long getLong(byte[] arr, int offset){
        if(offset + 8 <= arr.length){
            return  (long)arr[offset] << 56 |
                    (long)(arr[offset + 1] & 0xff) << 48 |
                    (long)(arr[offset + 2] & 0xff) << 40 |
                    (long)(arr[offset + 3] & 0xff) << 32 |
                    (arr[offset + 4] & 0xff) << 24 |
                    (arr[offset + 5] & 0xff) << 16 |
                    (arr[offset + 6] & 0xff) << 8 |
                    (arr[offset + 7] & 0xff);
        }
        return 0;
    }

    public static int getInteger(byte[] arr, int offset){
        if(offset + 4 <= arr.length){
            return arr[offset] << 24 |
                    (arr[1+offset] & 0xff) << 16 |
                    (arr[2+offset] & 0xff) << 8 |
                    (arr[3+offset] & 0xff);
        }
        return 0;
    }

    public byte[] encodeString(String str){
        return str.getBytes(StandardCharsets.UTF_8);
    }

    public String decodeString(byte[] data){
        return new String(data, StandardCharsets.UTF_8);
    }

}
