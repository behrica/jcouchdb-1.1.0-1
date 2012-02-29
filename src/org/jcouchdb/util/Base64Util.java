package org.jcouchdb.util;

public class Base64Util
{
    private final static char[] base64Chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

    public static String encodeBase64(byte[] data)
    {
        int cnt=data.length/3;

        StringBuilder buf=new StringBuilder(data.length+2);
        int off=0;
        for (int i=0; i < cnt; i++)
        {
          int v0=data[off];
          int v1=data[off+1];
          int v2=data[off+2];
          insertTriple(buf, v0, v1, v2);

          off+=3;
        }

        int remainder = data.length-cnt*3;
        switch (remainder)
        {
          case 2:
            insertTriple(buf, data[off], data[off+1], 0);
            buf.setCharAt(buf.length() -1 , '=');
            break;
          case 1:
            insertTriple(buf, data[off], 0, 0);
            buf.setCharAt(buf.length()-1 , '=');
            buf.setCharAt(buf.length()-2 , '=');
            break;
        }

        return buf.toString();
      }

      private static void insertTriple(StringBuilder buf, int v0, int v1, int v2)
      {
        // three bytes to four 6-bit values:
        // 11111122
        // 22223333
        // 33444444

        int b0= ( v0 & 0xfc) >> 2;
        int b1=(( v0 & 0x03) << 4)+((v1 & 0xf0) >> 4);
        int b2=(( v1 & 0x0f) << 2)+((v2 & 0xc0) >> 6);
        int b3= ( v2 & 0x3f);

        buf.append(base64Chars[b0])
        .append(base64Chars[b1])
        .append(base64Chars[b2])
        .append(base64Chars[b3]);
      }

}
