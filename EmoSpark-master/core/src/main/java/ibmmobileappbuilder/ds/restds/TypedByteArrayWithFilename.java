package ibmmobileappbuilder.ds.restds;

import retrofit.mime.TypedByteArray;

public class TypedByteArrayWithFilename extends TypedByteArray {

    private String fileName;

    public TypedByteArrayWithFilename(String mimeType, byte[] bytes, String fileName) {
        super(mimeType, bytes);
        this.fileName = fileName;
    }

    @Override public String fileName() {
        return fileName;
    }
}