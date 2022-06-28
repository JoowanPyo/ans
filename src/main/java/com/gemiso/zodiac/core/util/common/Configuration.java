package com.gemiso.zodiac.core.util.common;

import lombok.extern.slf4j.Slf4j;

import javax.naming.ConfigurationException;
import java.io.*;
import java.net.URL;
import java.util.Properties;
@Slf4j
public class Configuration extends AbstractConfiguration
{
    private String configFileName;

    public Configuration()
            throws ConfigurationException
    {
        initialize();
    }

    private void initialize()throws ConfigurationException{

        String configfileName = "";
        //Thread.currentThread().getContextClassLoader().getResource(name) 로 쓸수 있다
        //클레스 패스에서 resource를 찾는 방법이다.
        //null
        URL configUrl = ClassLoader.getSystemResource(File.separator+"data"+File.separator+"app"
                +File.separator+"ans"+File.separator+"server"+File.separator+"config"+File.separator+"config.properties");
        if (configUrl == null) {
            //C:\Users\JOOWAN PYO\npsnrcs\config\config.properties 내컴퓨터정보와 파라미터로 넣어준 디렉토리 주소가 들어감
            //user.home실행위치
            File defaultFile = new File(System.getProperty("file.separator"), File.separator+"data"+File.separator+"app"
                    +File.separator+"ans"+File.separator+"server"+File.separator+"config"+File.separator+"config.properties");
            log.info("defaultFile       :" +defaultFile);
            //C:\\Users\\JOOWAN PYO\\npsnrcs\\config\\config.properties
            //System.getProperty key값이 실행되는 위치를 String으로 출력
            configfileName = System.getProperty("app.config.file", defaultFile.getAbsolutePath());
            log.info("configFileName    :" +configfileName);
            //this.configFileName = configfileName;
        } else {
            File fileName = new File(configUrl.getFile());
            //파일경로를 가져온다
            configfileName = fileName.getAbsolutePath();
        }

        try
        {
            //시큐어코딩에 나온 심각사항. 파일경로 잘못들어가는거 방지.
           /* if (configfileName != null && !"".equals(configfileName)){
                configfileName = configfileName.replaceAll("/", File.separator); // "/" 필터링
                configfileName = configfileName.replaceAll("\\\\", File.separator); // "\" 필터링
                configfileName = configfileName.replaceAll("\\.\\.", File.separator); // ".." 필터링
            }*/

            File configFile = new File(configfileName);

            log.info("configFile          :"+configFile);
            if (!configFile.canRead()) {
                throw new ConfigurationException("Can't open configuration file: " + configfileName);
            }
            this.props = new Properties();

            //주어진 file 객체가 가리키는 파일을 바이트 스트림으로 읽기 위한 fileinputStream 객체를 생성
            FileInputStream fin = new FileInputStream(configFile);
            log.info("FileInputStream          :"+fin);
            try{
                this.props.load(new BufferedInputStream(fin));
            }
            catch (IOException e){
                log.error("IOException Occured "+e.getMessage());
            }
            finally
            {
                try{
                    if (fin != null) {
                        fin.close();
                    }
                }catch (IOException ex){
                    //System.out.println("BufferedReader 종료 중 에러 발생");
                    log.error("BufferedReader 종료 중 에러 발생 "+ex.getMessage());
                }
            }
        } catch (FileNotFoundException ex) {
            throw new ConfigurationException("Can't load configuration file: " + this.configFileName);
        } catch (IOException e) {
            //System.out.println("IOException Occured");
            log.error("IOException Occured " + e.getMessage());
        }
    }
}
