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

package opennlp.uima.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.uima.resource.ResourceInitializationException;

import opennlp.tools.ml.TrainerFactory;
import opennlp.tools.ml.maxent.GISModel;
import opennlp.tools.ml.model.TrainUtil;
import opennlp.tools.util.TrainingParameters;
import opennlp.tools.util.model.BaseModel;

/**
 * This class contains utils methods for the maxent library.
 */
final public class OpennlpUtil {
  private OpennlpUtil() {
    // this is util class must not be instantiated
  }

  /**
   * Serializes a {@link GISModel} and writes it to the given
   * {@link OutputStream}.
   *
   * @param model model to serialize
   * @throws IOException IOException
   */
  public static void serialize(BaseModel model, File modelFile)
      throws IOException {
    OutputStream modelOut = null;

    try {
      modelOut = new BufferedOutputStream(new FileOutputStream(modelFile));
      model.serialize(modelOut);
    } finally {
      if (modelOut != null)
        modelOut.close();
    }
  }

  public static final byte[] loadBytes(File inFile) throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();

    InputStream in = null;
    try {
      in = new FileInputStream(inFile);

      byte buffer[] = new byte[1024];
      int len;

      while ((len = in.read(buffer)) > 0) {
        bytes.write(buffer, 0, len);
      }
    }
    finally {
      if (in != null)
        in.close();
    }

    return bytes.toByteArray();
  }

  public static final TrainingParameters loadTrainingParams(String inFileValue,
      boolean isSequenceTrainingAllowed) throws ResourceInitializationException {

    TrainingParameters params;
    if (inFileValue != null) {
      InputStream paramsIn = null;
      try {
        paramsIn = new FileInputStream(new File(inFileValue));

        params = new opennlp.tools.util.TrainingParameters(paramsIn);
      } catch (IOException e) {
        throw new ResourceInitializationException(e);
      }
      finally {
        try {
          if (paramsIn != null)
            paramsIn.close();
        } catch (IOException e) {
        }
      }

      if (!TrainerFactory.isValid(params.getSettings())) {
        throw new ResourceInitializationException(new Exception("Training parameters file is invalid!"));
      }

      if (!isSequenceTrainingAllowed && TrainerFactory.isSequenceTraining(params.getSettings())) {
        throw new ResourceInitializationException(new Exception("Sequence training is not supported!"));
      }
    }
    else {
      params = TrainingParameters.defaultParams();
    }

    return params;
  }
}
