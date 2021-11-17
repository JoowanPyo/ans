package com.gemiso.zodiac.core.util;

import com.gemiso.zodiac.core.util.common.Config;
import com.gemiso.zodiac.core.util.common.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.io.File;

@Slf4j
public class PropertyUtil {
    //private static final Logger logger = Logger.getLogger("com.common");

    public UploadFileBean getUploadInfo(String fn, String target)
    {
           UploadFileBean pb = new UploadFileBean();
        Document doc = null;

         SAXBuilder builder = new SAXBuilder();
        try
        {
             if (log.isDebugEnabled())
            {
                log.debug("config root: " + getConfigRoot());
                log.debug("config file name: " + fn);
            }
            //file:/C:/FileAttach.xml
             doc = builder.build(new File(getConfigRoot(), fn));
            log.info("doc           :" +doc);
            Element root = doc.getRootElement();

            Element divcd = root.getChild("uploads").getChild(target).getChild("divcd");
            Element up_yn = root.getChild("uploads").getChild(target).getChild("up_yn");
            Element dest = root.getChild("uploads").getChild(target).getChild("dest");
            Element updir = root.getChild("uploads").getChild(target).getChild("updir");
            Element maxsize = root.getChild("uploads").getChild(target).getChild("maxsize");
            Element dirtype = root.getChild("uploads").getChild(target).getChild("dirtype");
            Element rname_yn = root.getChild("uploads").getChild(target).getChild("rname_yn");

            pb.setDivcd(divcd.getValue());
            pb.setUp_yn(up_yn.getValue());
            pb.setDest(dest.getValue());
            pb.setUpdir(updir.getValue());
            pb.setMaxsize(maxsize.getValue());
            pb.setDirtype(dirtype.getValue());
            pb.setRname_yn(rname_yn.getValue());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return pb;
    }

    public static String getConfigRoot() throws Exception
    {
        String configpaths = "";
        try
        {
            Config conf = new Configuration();
            configpaths = conf.getString("configpath");

            log.info("configpaths : " + configpaths);
        }
        catch (Exception localException) {
        }
        return configpaths;
    }
}
