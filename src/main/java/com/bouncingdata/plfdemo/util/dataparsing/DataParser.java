package com.bouncingdata.plfdemo.util.dataparsing;

import java.io.InputStream;
import java.util.List;

/**
 *
 */
public interface DataParser {  
  
  /**
   * @param is the input stream
   * @return 
   * @throws Exception
   */
  public List<DatasetColumn> parseSchema(InputStream is) throws Exception;
  
  /**
   * @param is
   * @return
   * @throws Exception
   */
  public List<Object[]> parse(InputStream is) throws Exception;
}
