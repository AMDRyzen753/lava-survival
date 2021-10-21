package link.therealdomm.heldix.lavasurvival.util.configuration;

/**
 * a interface for simple json config implementation
 *
 * @author TheRealDomm
 * @since 11.10.2021
 */
public interface Config {

    /**
     * @return the saved config version
     */
    double getConfigVersion();

    /**
     * set a new config version
     * @param version to set
     */
    void setConfigVersion(double version);

}
