package tv.spielemeister.mpg.engine.net;

import java.nio.charset.StandardCharsets;

public abstract class NetPacket {

    public byte packetType;

    public NetPacket(byte[] data){
        parse(data);
        this.packetType = data[0];
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
            arr[offset] = (byte) (val >> 56);
            arr[1+offset] = (byte) (val >> 48);
            arr[2+offset] = (byte) (val >> 40);
            arr[3+offset] = (byte) (val >> 32);
            arr[4+offset] = (byte) (val >> 24);
            arr[5+offset] = (byte) (val >> 16);
            arr[6+offset] = (byte) (val >> 8);
            arr[7+offset] = (byte) (val);
        }
    }

    public static void put(byte[] arr, int val, int offset){
        if(offset + 7 < arr.length) {
            arr[offset] = (byte) (val >> 24);
            arr[1+offset] = (byte) (val >> 16);
            arr[2+offset] = (byte) (val >> 8);
            arr[3+offset] = (byte) (val);
        }
    }

    public static byte[] get(byte[] arr, int offset, int len){
        byte[] ret = new byte[len];
        if(offset+len <= arr.length){
            System.arraycopy(arr, offset, ret, 0, len);
        }
        return ret;
    }

    public static byte[] getRest(byte[] arr, int offset){
        return get(arr, offset, arr.length - offset);
    }

    public static long getLong(byte[] arr, int offset){
        if(offset + 7 < arr.length){
            return (long) arr[offset] << 56 |
                    (long) arr[offset + 1] << 48 |
                    (long) arr[offset + 2] << 40 |
                    (long) arr[offset + 3] << 32 |
                    arr[offset + 4] << 24 |
                    arr[offset + 5] << 16 |
                    arr[offset + 6] << 8 |
                    arr[offset + 7];
        }
        return 0;
    }

    public static int getInteger(byte[] arr, int offset){
        if(offset + 3 < arr.length){
            return arr[offset] << 24 |
                    arr[1+offset] << 16 |
                    arr[2+offset] << 8 |
                    arr[3+offset];
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
