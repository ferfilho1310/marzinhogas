package br.com.marzinhogas.Helpers;

public class AccessResources implements IAccessResources {

    public static AccessResources accessResources;

    private AccessResources() {
    }

    public static synchronized AccessResources getInstance() {
        if (accessResources == null) {
            accessResources = new AccessResources();
        }
        return accessResources;
    }

    @Override
    public String criptografiadesenha(String user,String senha) {
        String sign = user + senha;

        try {
            java.security.MessageDigest md =
                    java.security.MessageDigest.getInstance("MD5");
            md.update(sign.getBytes());
            byte[] hash = md.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                if ((0xff & hash[i]) < 0x10)
                    hexString.append(
                            "0" + Integer.toHexString((0xFF & hash[i])));
                else
                    hexString.append(Integer.toHexString(0xFF & hash[i]));
            }
            sign = hexString.toString();
        }
        catch (Exception nsae) {
            nsae.printStackTrace();
        }
        return sign;
    }
}
