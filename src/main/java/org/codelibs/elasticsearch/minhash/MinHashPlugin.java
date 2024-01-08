package org.codelibs.elasticsearch.minhash;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.codelibs.elasticsearch.minhash.index.analysis.NumbertoWordTokenFilterFactory;
import org.codelibs.elasticsearch.minhash.index.mapper.NumbertoWordFieldMapper;
import org.codelibs.elasticsearch.minhash.index.analysis.MinHashTokenFilterFactory;
import org.codelibs.elasticsearch.minhash.index.mapper.MinHashFieldMapper;
import org.elasticsearch.index.analysis.TokenFilterFactory;
import org.elasticsearch.index.mapper.Mapper;
import org.elasticsearch.indices.analysis.AnalysisModule.AnalysisProvider;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.MapperPlugin;
import org.elasticsearch.plugins.Plugin;

public class MinHashPlugin extends Plugin implements MapperPlugin, AnalysisPlugin {

    @Override
    public Map<String, AnalysisProvider<TokenFilterFactory>> getTokenFilters() {
        final Map<String, AnalysisProvider<TokenFilterFactory>> extra = new HashMap<>();
        
        extra.put("minhash", MinHashTokenFilterFactory::new);       
        extra.put("numbertoword", NumbertoWordTokenFilterFactory::new);
        return extra;
    }
    
    
    @Override
    public Map<String, Mapper.TypeParser> getMappers() {
    
    
            Map<String, Mapper.TypeParser> s= new HashMap<String, Mapper.TypeParser>();
            s.put(MinHashFieldMapper.CONTENT_TYPE, new MinHashFieldMapper.TypeParser());
            s.put(NumbertoWordFieldMapper.CONTENT_TYPE, new NumbertoWordFieldMapper.TypeParser());
    
          return s;
    }

}


