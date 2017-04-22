package com.skp.util;

import java.io.UnsupportedEncodingException;
import java.util.zip.CRC32;

public class DBHash {

    private static ThreadLocal<CRC32> crc32Provider = new ThreadLocal<CRC32>() {
                                                        @Override
                                                        protected CRC32 initialValue() {
                                                            return new CRC32();
                                                        }
                                                    };

    public static int getHash4split(long id, int splitCount) {
        try {
            long h = getCrc32(String.valueOf(id).getBytes("utf-8"));
            if (h < 0) {
                h = -1 * h;
            }
            int hash = (int) (h / splitCount % splitCount);
            return hash;
        } catch (UnsupportedEncodingException e) {
            return -1;
        }
    }

    public static long getCrc32(byte[] b) {
        CRC32 crc = crc32Provider.get();
        crc.reset();
        crc.update(b);
        return crc.getValue();
    }

    public static int getHash(byte[] b, int dbcount, int tableCount) {
        int splitCount = dbcount * tableCount;
        return getHash4split(getCrc32(b), splitCount);
    }
}
