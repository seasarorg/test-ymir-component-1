package org.seasar.ymir;

public enum HttpMethod {
    /** CONNECTメソッドを表す値です。 */
    CONNECT,

    /** DELETEメソッドを表す値です。 */
    DELETE,

    /** GETメソッドを表す値です。 */
    GET,

    /** HEADメソッドを表す値です。 */
    HEAD,

    /** LINKメソッドを表す値です。 */
    LINK,

    /** OPTIONSメソッドを表す値です。 */
    OPTIONS,

    /** PATCHメソッドを表す値です。 */
    PATCH,

    /** POSTメソッドを表す値です。 */
    POST,

    /** PUTメソッドを表す値です。 */
    PUT,

    /** TRACEメソッドを表す値です。 */
    TRACE,

    /** UNLINKメソッドを表す値です。 */
    UNLINK;

    public static HttpMethod enumOf(String name) {
        if (name == null) {
            return null;
        }
        return valueOf(name.toUpperCase());
    }
}
