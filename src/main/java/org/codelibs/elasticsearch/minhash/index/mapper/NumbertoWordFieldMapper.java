package org.codelibs.elasticsearch.minhash.index.mapper;

import static org.elasticsearch.common.xcontent.support.XContentMapValues.isArray;
import static org.elasticsearch.common.xcontent.support.XContentMapValues.nodeStringValue;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.SortedSetDocValuesField;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.common.lucene.Lucene;
import org.elasticsearch.index.analysis.NamedAnalyzer;
import org.elasticsearch.index.fielddata.IndexFieldData;
import org.elasticsearch.index.fielddata.plain.SortedSetOrdinalsIndexFieldData;
import org.elasticsearch.index.mapper.DocumentParserContext;
import org.elasticsearch.index.mapper.FieldMapper;
import org.elasticsearch.index.mapper.MappedFieldType;
import org.elasticsearch.index.mapper.Mapper;
import org.elasticsearch.index.mapper.MapperBuilderContext;
import org.elasticsearch.index.mapper.MapperParsingException;
import org.elasticsearch.index.mapper.MappingParserContext;
import org.elasticsearch.index.mapper.SourceValueFetcher;
import org.elasticsearch.index.mapper.StringFieldType;
import org.elasticsearch.index.mapper.TextSearchInfo;
import org.elasticsearch.index.mapper.ValueFetcher;
import org.elasticsearch.index.query.SearchExecutionContext;
import org.elasticsearch.search.aggregations.support.CoreValuesSourceType;
import org.elasticsearch.search.lookup.SearchLookup;
import org.elasticsearch.xcontent.XContentParser;


public class NumbertoWordFieldMapper extends FieldMapper {

    public static final String CONTENT_TYPE = "numbertoword";

    public static class Defaults {
        public static final FieldType FIELD_TYPE = new FieldType();

        static {
            FIELD_TYPE.setTokenized(false);
            FIELD_TYPE.setOmitNorms(true);
            FIELD_TYPE.setIndexOptions(IndexOptions.DOCS);
            FIELD_TYPE.freeze();
        }
    }

    public static class NumbertoWordField extends Field {
        public NumbertoWordField(final String field, final CharSequence term,
                final FieldType ft) {
            super(field, term, ft);
        }
    }

    private static NumbertoWordFieldMapper toType(final FieldMapper in) {
        return (NumbertoWordFieldMapper) in;
    }

    public static class Builder extends FieldMapper.Builder {

        private final Parameter<Boolean> indexed = Parameter
                .indexParam(m -> toType(m).indexed, true);

        private final Parameter<Boolean> hasDocValues = Parameter
                .docValuesParam(m -> toType(m).hasDocValues, true);

        private final Parameter<Boolean> stored = Parameter
                .storeParam(m -> toType(m).stored, false);

        private final Parameter<String> nullValue = Parameter.stringParam(
                "null_value", false, m -> toType(m).nullValue, null);

        private final Parameter<Boolean> bitString = Parameter.boolParam(
                "bit_string", false, m -> toType(m).bitString, false);

        private final Parameter<Map<String, String>> meta = Parameter
                .metaParam();

        private final Parameter<String> NumbertoWordAnalyzer = Parameter
                .stringParam("NumbertoWord_analyzer", true, m -> {
                    final NamedAnalyzer NumbertoWordAnalyzer = toType(
                            m).NumbertoWordAnalyzer;
                    if (NumbertoWordAnalyzer != null) {
                        return NumbertoWordAnalyzer.name();
                    }
                    return "standard";
                }, "standard");

        @Deprecated
        private final Parameter<String[]> copyBitsTo = new Parameter<>(
                "copy_bits_to", true, () -> new String[0],
                (n, c, o) -> parseCopyBitsFields(o), m -> new String[0]);

        private final MappingParserContext parserContext;

        private NamedAnalyzer mergedAnalyzer;

        public Builder(final String name,
                final MappingParserContext parserContext) {
            super(name);
            this.parserContext = parserContext;
        }

        @Override
        public List<Parameter<?>> getParameters() {
            return Arrays.asList(meta, indexed, stored, hasDocValues, nullValue,
                    bitString, NumbertoWordAnalyzer, copyBitsTo);
        }

        @Override
        public Builder init(final FieldMapper initializer) {
            super.init(initializer);
            if (initializer instanceof NumbertoWordFieldMapper) {
                final NumbertoWordFieldMapper mapper = (NumbertoWordFieldMapper) initializer;
                this.indexed.setValue(mapper.indexed);
                this.hasDocValues.setValue(mapper.hasDocValues);
                this.nullValue.setValue(mapper.nullValue);
                this.bitString.setValue(mapper.bitString);
                this.mergedAnalyzer = mapper.NumbertoWordAnalyzer;
            }
            return this;
        }

        private NamedAnalyzer NumbertoWordAnalyzer() {
            if (mergedAnalyzer != null) {
                return mergedAnalyzer;
            }
            if (parserContext != null) {
                return parserContext.getIndexAnalyzers()
                        .get(NumbertoWordAnalyzer.getValue());
            }
            return null;
        }

        private NumbertoWordFieldType buildFieldType(final MapperBuilderContext context,
                final FieldType fieldType) {
            return new NumbertoWordFieldType(context.buildFullName(name), fieldType,
                    indexed.getValue(), stored.getValue(),
                    hasDocValues.getValue(), meta.getValue());
        }

        @Override
        public NumbertoWordFieldMapper build(final MapperBuilderContext context) {
            final FieldType fieldtype = new FieldType(
                    NumbertoWordFieldMapper.Defaults.FIELD_TYPE);
            fieldtype.setIndexOptions(
                    indexed.getValue() ? IndexOptions.DOCS : IndexOptions.NONE);
            fieldtype.setStored(this.stored.getValue());
            return new NumbertoWordFieldMapper(name, fieldtype,
                    buildFieldType(context, fieldtype),
                    multiFieldsBuilder.build(this, context), copyTo.build(),
                    this, NumbertoWordAnalyzer());
        }
    }

    public static class TypeParser implements Mapper.TypeParser {
        @Override
        public NumbertoWordFieldMapper.Builder parse(final String name,
                final Map<String, Object> node,
                final MappingParserContext parserContext)
                throws MapperParsingException {
            final NumbertoWordFieldMapper.Builder builder = new NumbertoWordFieldMapper.Builder(
                    name, parserContext);
            builder.parse(name, parserContext, node);
            return builder;
        }
    }

    @Deprecated
    public static String[] parseCopyBitsFields(final Object propNode) {
        if (isArray(propNode)) {
            @SuppressWarnings("unchecked")
            final List<Object> nodeList = (List<Object>) propNode;
            return nodeList.stream().map(o -> nodeStringValue(o, null))
                    .filter(s -> s != null).toArray(n -> new String[n]);
        }
        return new String[] { nodeStringValue(propNode, null) };
    }

    public static final class NumbertoWordFieldType extends StringFieldType {
        public NumbertoWordFieldType(final String name, final FieldType fieldType,
                final boolean isIndexed, final boolean isStored,
                final boolean hasDocValues, final Map<String, String> meta) {
            super(name, isIndexed, isStored, hasDocValues,
                    new TextSearchInfo(fieldType, null, Lucene.KEYWORD_ANALYZER,
                            Lucene.KEYWORD_ANALYZER),
                    meta);
        }

        @Override
        public String typeName() {
            return CONTENT_TYPE;
        }

        @Override
        public ValueFetcher valueFetcher(final SearchExecutionContext context,
                final String format) {
            return SourceValueFetcher.identity(name(), context, format);
        }

        @Override
        public IndexFieldData.Builder fielddataBuilder(
                final String fullyQualifiedIndexName,
                final Supplier<SearchLookup> searchLookup) {
            failIfNoDocValues();
            return new SortedSetOrdinalsIndexFieldData.Builder(name(),
                    CoreValuesSourceType.KEYWORD);
        }

        @Override
        public CollapseType collapseType() {
            return CollapseType.KEYWORD;
        }
    }

    private final boolean indexed;

    private final boolean stored;

    private final boolean hasDocValues;

    private final String nullValue;

    private final boolean bitString;

    private final NamedAnalyzer NumbertoWordAnalyzer;

    private final FieldType fieldType;

    protected NumbertoWordFieldMapper(final String simpleName,
            final FieldType fieldType, final MappedFieldType mappedFieldType,
            final MultiFields multiFields, final CopyTo copyTo,
            final Builder builder, final NamedAnalyzer NumbertoWordAnalyzer) {
        super(simpleName, mappedFieldType, multiFields, copyTo);
        this.indexed = builder.indexed.getValue();
        this.stored = builder.stored.getValue();
        this.hasDocValues = builder.hasDocValues.getValue();
        this.nullValue = builder.nullValue.getValue();
        this.bitString = builder.bitString.getValue();
        this.NumbertoWordAnalyzer = NumbertoWordAnalyzer;
        this.fieldType = fieldType;
    }

    @Override
    protected void parseCreateField(final DocumentParserContext context)
            throws IOException {
        if (!indexed && !stored && !hasDocValues) {
            return;
        }

        String value;
        final XContentParser parser = context.parser();
        if (parser.currentToken() == XContentParser.Token.VALUE_NULL) {
            value = nullValue;
        } else {
            value = parser.textOrNull();
        }

        if (value == null) {
            return;
        }

        final String stringValue;
        if (bitString) {
            stringValue = "Bye";
        } else {
            stringValue = NumberUtils.numberToWords(Long.parseLong(value));
        }


        if (indexed || stored) {
        
              String outputValue = NumberUtils.numberToWords(Long.parseLong(value));
        
        
            final IndexableField field = new NumbertoWordField(fieldType().name(),outputValue, fieldType);
            context.doc().add(field);

            if (!hasDocValues) {
                context.addToFieldNames(fieldType().name());
            }
        }

        if (hasDocValues) {
            final BytesRef binaryValue = new BytesRef(stringValue);
            context.doc().add(new SortedSetDocValuesField(fieldType().name(),
                    binaryValue));
        }
    }

    @Override
    public FieldMapper.Builder getMergeBuilder() {
        return new NumbertoWordFieldMapper.Builder(simpleName(), null).init(this);
    }

    @Override
    protected String contentType() {
        return CONTENT_TYPE;
    }
}
