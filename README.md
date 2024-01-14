Elasticsearch MinHash Plugin
[![Java CI with Maven](https://github.com/codelibs/elasticsearch-minhash/actions/workflows/maven.yml/badge.svg)](https://github.com/codelibs/elasticsearch-minhash/actions/workflows/maven.yml)
=======================

## Overview

MinHash Plugin provides b-bit MinHash algorithm for Elasticsearch.
Using a field type and a token filter provided by this plugin, you can add a minhash value to your document.

## Version

[Versions in Maven Repository](https://repo1.maven.org/maven2/org/codelibs/elasticsearch-minhash/)

### Issues/Questions

Please file an [issue](https://github.com/codelibs/elasticsearch-minhash/issues "issue").

## Installation

    $ $ES_HOME/bin/elasticsearch-plugin install org.codelibs:elasticsearch-minhash:7.14.0

## Getting Started

### Add MinHash Analyzer

First, you need to add a minhash analyzer when creating your index:

    $ curl -XPUT 'localhost:9200/my_index' -d '{
      "index":{
        "analysis":{
          "analyzer":{
            "minhash_analyzer":{
              "type":"custom",
              "tokenizer":"standard",
              "filter":["minhash"]
            }
          }
        }
      }
    }'

You are free to change tokenizer/char\_filter/filter settings, but the minhash filter needs to be added as a last filter.

### Add MinHash field

Put a minhash field into an index mapping:

    $ curl -XPUT "localhost:9200/my_index/_mapping" -d '{
      "properties":{
        "message":{
          "type":"string",
          "copy_to":"minhash_value"
        },
        "minhash_value":{
          "type":"minhash",
          "store":true,
          "minhash_analyzer":"minhash_analyzer"
        }
      }
    }'

The field type of minhash is of binary type.
The above example calculates a minhash value of the message field and stores it in the minhash\_value field.

## Get MinHash Value

Add the following document:

    $ curl -XPUT "localhost:9200/my_index/_doc/1" -d '{
      "message":"Fess is Java based full text search server provided as OSS product."
    }'

The minhash value is calculated automatically when adding the document.
You can check it as below:

    $ curl -XGET "localhost:9200/my_index/_doc/1?pretty&stored_fields=minhash_value,_source"

The response is:

    {
      "_index" : "my_index",
      "_type" : "_doc",
      "_id" : "1",
      "_version" : 1,
      "found" : true,
      "_source":{
          "message":"Fess is Java based full text search server provided as OSS product."
        },
      "fields" : {
        "minhash_value" : [ "KV5rsUfZpcZdVojpG8mHLA==" ]
      }
    }

## References

### Change the number of bits and hashes

To change the number of bits and hashes, set them to a token filter setting:

    $ curl -XPUT 'localhost:9200/my_index' -d '{
      "index":{
        "analysis":{
          "analyzer":{
            "minhash_analyzer":{
              "type":"custom",
              "tokenizer":"standard",
              "filter":["my_minhash"]
            }
          }
        },
        "filter":{
          "my_minhash":{
            "type":"minhash",
            "seed":100,
            "bit":2,
            "size":32
          }
        }
      }
    }'

### GET _search
    {
      "query": {
        "match_all": {}
      }
    }




### Change the Complitly        

    DELETE crimes
    
    POST /crimes/_close
    
    
    PUT /crimes/_settings
    {
        "analysis": {
          "analyzer": {
            "minhash_analyzer":{
              "type":"custom",
              "tokenizer":"standard",
              "filter":["minhash"]
            }
          }
        }
    }
    
    PUT /crimes/_mappings
    {
       "properties":{
        "datetime2":{
          "type":"date",
          "copy_to":"minhash_value"
        },
        "minhash_value":{
          "type":"minhash",
          "store":true,
          "minhash_analyzer":"minhash_analyzer"
        }
       }
    }
    
    
    POST /crimes/_open
    
    
     POST /crimes/_search
    {  
     "query":{  
       "match_all":{  
    
       }
     }
    }
    
    

    
    POST crimes/_update_by_query
    {
      "query": {
            "constant_score" : {
                "filter" : {
                    "exists" : { "field" : "datetime" }
                }
            }
    
      },
      "script" : {
          "inline": "SimpleDateFormat sdf = new SimpleDateFormat(\"yyyy-mm-dd hh:mm:ss\");  ctx._source.datetime2 = sdf.parse(ctx._source.datetime);"
      }
    }
    
    
    
    
    POST /crimes/_search?pretty&stored_fields=minhash_value,_source
    
    
    POST /crimes/_search
    {  
     "query":{  
       "match_all":{  
    
       }
     },
     "script_fields":{  
       "aDate":{  
          "script":"doc['minhash_value'].value"
       }
     }
    }

### Change the Complitly   
    DELETE /my_index
    
    PUT /my_index
    {
      "settings": {
        "number_of_shards": 1,
        "number_of_replicas": 0,
        "index.max_ngram_diff": 10,
        "analysis": {
          "analyzer": {
            "NumbertoWord_analyzer":{
              "type":"custom",
              "tokenizer":"standard",
              "filter":["numbertoword"]
            }
          }
        }
      },
      "mappings": {
        "properties":{
        "message":{
          "type":"keyword",
          "copy_to":"NumbertoWord_value"
        },
        "NumbertoWord_value":{
          "type":"numbertoword",
          "store":true,
          "NumbertoWord_analyzer":"NumbertoWord_analyzer"
        }
       }
      }
    }
    
    
    
    POST /_bulk
    { "index" : { "_index" : "my_index", "_id" : "1" } }
    { "message":"1234" }
    
    GET /my_index/_doc/1?pretty&stored_fields=NumbertoWord_value,_source


        

The above allows to set the number of bits to 2, the number of hashes to 32 and the seed of hash to 100.

