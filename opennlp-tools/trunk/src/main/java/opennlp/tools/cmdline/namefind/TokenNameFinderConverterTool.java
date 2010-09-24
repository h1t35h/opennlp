/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package opennlp.tools.cmdline.namefind;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import opennlp.tools.cmdline.AbstractConverterTool;
import opennlp.tools.cmdline.ObjectStreamFactory;
import opennlp.tools.formats.ADNameSampleStreamFactory;
import opennlp.tools.formats.Conll02NameSampleStreamFactory;
import opennlp.tools.namefind.NameSample;

/**
 * Tool to convert multiple data formats into native opennlp name finder training
 * format.
 */
public class TokenNameFinderConverterTool extends AbstractConverterTool<NameSample> {

  private static final Map<String, ObjectStreamFactory<NameSample>> streamFactories;
  
  static {
    Map<String, ObjectStreamFactory<NameSample>> mutableStreamFactories =
      new HashMap<String, ObjectStreamFactory<NameSample>>();
    
    mutableStreamFactories.put("conll02", new Conll02NameSampleStreamFactory());
    mutableStreamFactories.put("ad", new ADNameSampleStreamFactory());
    
    streamFactories = Collections.unmodifiableMap(mutableStreamFactories);
  }
  
  public String getName() {
    return "TokenNameFinderConverter";
  }

  public String getShortDescription() {
    return "converts foreign data formats to native format";
  }
  
  @Override
  protected ObjectStreamFactory<NameSample> createStreamFactory(String format) {
    return streamFactories.get(format);
  }
}
