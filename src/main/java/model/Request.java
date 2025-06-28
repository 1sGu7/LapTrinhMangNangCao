package model;

import java.io.Serializable;
import java.util.Arrays;

public class Request implements Serializable {
    private String message;
    private String signature;
    private byte[] encryptedPublicKey;
    private byte[] aesKeyBytes;
    private byte[] iv;

    public Request(String message, String signature,
                   byte[] encryptedPublicKey,
                   byte[] aesKeyBytes, byte[] iv) {
        this.message = message;
        this.signature = signature;
        this.encryptedPublicKey = encryptedPublicKey;
        this.aesKeyBytes = aesKeyBytes;
        this.iv = iv;
    }

    public String getMessage() { return message; }
    public String getSignature() { return signature; }
    public byte[] getEncryptedPublicKey() { return encryptedPublicKey; }
    public byte[] getAesKeyBytes() { return aesKeyBytes; }
    public byte[] getIv() { return iv; }

    @Override
    public String toString() {
        return "Request{" +
                "message='" + message + '\'' +
                ", signature='" + signature + '\'' +
                ", encryptedPublicKey=" + Arrays.toString(encryptedPublicKey) +
                ", aesKeyBytes=" + Arrays.toString(aesKeyBytes) +
                ", iv=" + Arrays.toString(iv) +
                '}';
    }
}