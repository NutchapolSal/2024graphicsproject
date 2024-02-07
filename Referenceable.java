
interface Referenceable extends Exportable {
    public String getCodeId();

    public String exportInitCode();

    public String exportInitString();

    /** must be a reference to the object */
    public String exportCode();

    /** must be a reference to the object */
    public String exportString();
}