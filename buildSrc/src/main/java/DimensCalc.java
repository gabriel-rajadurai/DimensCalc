import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import groovy.util.Node;
import groovy.util.XmlNodePrinter;
import groovy.util.XmlParser;


/**
 * Created by Gabriel on 30-10-2017.
 */

public class DimensCalc {


    public static void calcDimens(String projPath) {
        float[] dimenFactor = new float[]{0.89f, 1.1f, 1.3f, 1.67f, 2.0f};
        try {

            XmlParser parser = (new XmlParser());

            //Source dimen file
            File f = new File(projPath + "/src/main/res/values/dimens.xml");

            //New dimen files
            File[] nFiles = new File[5];
            nFiles[0] = new File(projPath + "/src/main/res/values-sw320dp/dimens.xml");
            nFiles[1] = new File(projPath + "/src/main/res/values-sw400dp/dimens.xml");
            nFiles[2] = new File(projPath + "/src/main/res/values-sw480dp/dimens.xml");
            nFiles[3] = new File(projPath + "/src/main/res/values-sw600dp/dimens.xml");
            nFiles[4] = new File(projPath + "/src/main/res/values-sw720dp/dimens.xml");

            for (File nFile : nFiles) {
                nFile.getParentFile().mkdirs();
                nFile.createNewFile();
            }

            //Source
            FileChannel source;
            List sNode = parser.parse(f).children();

            //Destinations
            FileChannel[] destination = new FileChannel[5];
            Node[] fxml = new Node[5];
            List[] dNodes = new List[5];


            // Initialize the source and destination
            for (int i = 0; i < 5; i++) {
                source = new FileInputStream(f).getChannel();
                destination[i] = new FileOutputStream(nFiles[i]).getChannel();
                destination[i].transferFrom(source, 0, source.size());
                fxml[i] = parser.parse(nFiles[i]);
                dNodes[i] = fxml[i].children();
            }


            //Calculate dimen values
            for (int i = 0; i < sNode.size(); i++) {
                for (int j = 0; j < 5; j++) {
                    Object dpValueObject = ((Node) sNode.get(i)).value();
                    String dpValue = dpValueObject.toString();
                    String newdp = dpValue.replace("dp", "");
                    newdp = newdp.replace("sp", "");
                    newdp = newdp.replace("[", "");
                    newdp = newdp.replace("]", "");

                    double dp = Double.parseDouble(newdp);
                    DecimalFormat df = new DecimalFormat("0.00");
                    if (dpValue.contains("dp")) {
                        ((Node) dNodes[j].get(i)).setValue(df.format(dimenFactor[j] * dp) + "dp");

                    } else if (dpValue.contains("sp")) {
                        ((Node) dNodes[j].get(i)).setValue(df.format(dimenFactor[j] * dp) + "sp");
                    }
                }
            }

            //Writing output to file
            for (int i = 0; i < 5; i++) {
                new XmlNodePrinter(new PrintWriter(new FileWriter(nFiles[i]))).print(fxml[i]);
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}
