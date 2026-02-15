package pl.hubertmaka.culinaryagent.domain.enums;

public enum FileExtension {
    JPEG("jpeg"),
    JPG("jpg"),
    PNG("png");

    private final String extension;

    FileExtension(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
}

