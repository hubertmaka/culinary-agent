package pl.hubertmaka.culinaryagent.domain.enums;

/**
 * Enum representing supported file extensions for images.
 */
public enum FileExtension {
    JPEG("jpeg"),
    JPG("jpg"),
    PNG("png");

    /** The string representation of the file extension. */
    private final String extension;

    /**
     * Constructs a FileExtension enum with the specified string representation.
     *
     * @param extension the string representation of the file extension
     */
    FileExtension(String extension) { this.extension = extension; }

    /**
     * Returns the string representation of the file extension.
     *
     * @return the file extension as a string
     */
    public String getExtension() { return extension; }
}

