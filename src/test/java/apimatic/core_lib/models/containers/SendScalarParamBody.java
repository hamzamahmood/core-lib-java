/*
 * TypeCombinatorModerateLib
 *
 * This file was automatically generated by APIMATIC v3.0 ( https://www.apimatic.io ).
 */

package apimatic.core_lib.models.containers;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.apimatic.core_lib.annotations.TypeCombinator.TypeCombinatorCase;
import io.apimatic.core_lib.annotations.TypeCombinator.TypeCombinatorStringCase;
import io.apimatic.core_lib.utilities.CoreHelper;

/**
 * This is a container class for any-of types.
 */
@JsonDeserialize(using = SendScalarParamBody.SendScalarParamBodyDeserializer.class)
public abstract class SendScalarParamBody {

    /**
     * Any-of type initialization method.
     * 
     * @param precision List of Double value for precision.
     * @return The PrecisionCase object.
     */
    public static SendScalarParamBody fromPrecision(List<Double> precision) {
        return precision == null ? null : new PrecisionCase(precision);
    }

    /**
     * Any-of type initialization method.
     * 
     * @param mString String value for mString.
     * @return The MStringCase object.
     */
    public static SendScalarParamBody fromMString(String mString) {
        return mString == null ? null : new MStringCase(mString);
    }

    /**
     * Method to match from the provided any-of cases.
     * 
     * @param <R> The type to return after applying callback.
     * @param cases The any-of type cases callback.
     * @return The any-of matched case.
     */
    public abstract <R> R match(Cases<R> cases);

    /**
     * Method to get serialized content type of set any-of type.
     * 
     * @return The String value of content type.
     */
    public abstract String getContentType();

    /**
     * This is interface for any-of cases.
     * 
     * @param <R> The type to return after applying callback.
     */
    public interface Cases<R> {
        R precision(List<Double> precision);

        R mString(String mString);
    }

    /**
     * This is a implementation class for PrecisionCase.
     */
    @JsonDeserialize(using = JsonDeserializer.None.class)
    @TypeCombinatorCase
    private static class PrecisionCase extends SendScalarParamBody {

        @JsonValue
        private List<Double> precision;

        PrecisionCase(List<Double> precision) {
            this.precision = precision;
        }

        @Override
        public <R> R match(Cases<R> cases) {
            return cases.precision(this.precision);
        }

        @Override
        public String getContentType() {
            return "application/json";
        }

        @JsonCreator
        private PrecisionCase(JsonNode jsonNode) throws IOException {
            this.precision = CoreHelper.deserializeArray(jsonNode, Double[].class);
        }

        @Override
        public String toString() {
            return precision.toString();
        }
    }

    /**
     * This is a implementation class for MStringCase.
     */
    @JsonDeserialize(using = JsonDeserializer.None.class)
    @TypeCombinatorStringCase
    @TypeCombinatorCase
    private static class MStringCase extends SendScalarParamBody {

        @JsonValue
        private String mString;

        MStringCase(String mString) {
            this.mString = mString;
        }

        @Override
        public <R> R match(Cases<R> cases) {
            return cases.mString(this.mString);
        }

        @Override
        public String getContentType() {
            return "text/plain; charset=utf-8";
        }

        @JsonCreator
        private MStringCase(JsonNode jsonNode) throws IOException {
            if (jsonNode.isTextual()) {
                this.mString = CoreHelper.deserialize(jsonNode, String.class);
            } else {
                throw new IllegalArgumentException();
            }
        }

        @Override
        public String toString() {
            return mString.toString();
        }
    }

    /**
     * This is a custom deserializer class for SendScalarParamBody.
     */
    protected static class SendScalarParamBodyDeserializer
            extends JsonDeserializer<SendScalarParamBody> {

        @Override
        public SendScalarParamBody deserialize(JsonParser jp, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            ObjectCodec oc = jp.getCodec();
            JsonNode node = oc.readTree(jp);
            return CoreHelper.deserialize(node,
                    Arrays.asList(PrecisionCase.class, MStringCase.class), false);
        }
    }

}
