package instinct2026.Constants;

import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;

public class EPAConsts {

    public static class EPA_Conversion_Tree {

        public static InterpolatingDoubleTreeMap epaConversionTable = new InterpolatingDoubleTreeMap();

            public static void setupMaps(){
                //key = unitless EPA, value = EPA
                epaConversionTable.put(1465.0,30.7);
                epaConversionTable.put(1470.0,32.9);
                epaConversionTable.put(1475.0,34.6);
                epaConversionTable.put(1480.0,36.5);
                epaConversionTable.put(1485.0,38.5);
                epaConversionTable.put(1490.0,40.2);
                epaConversionTable.put(1495.0,42.0);
                epaConversionTable.put(1505.0,45.8);
                epaConversionTable.put(1510.0,47.7);
                epaConversionTable.put(1515.0,49.6);
                epaConversionTable.put(1520.0,51.5);
                epaConversionTable.put(1525.0,53.5);
                epaConversionTable.put(1530.0,55.3);
                epaConversionTable.put(1535.0,57.2);
                epaConversionTable.put(1540.0,59.0);
                epaConversionTable.put(1545.0,60.9);
                epaConversionTable.put(1550.0,62.9);
                epaConversionTable.put(1555.0,64.7);
                epaConversionTable.put(1560.0,66.6);
                epaConversionTable.put(1565.0,68.4);
                epaConversionTable.put(1570.0,70.4);
                epaConversionTable.put(1575.0,72.2);
                epaConversionTable.put(1585.0,75.8);
                epaConversionTable.put(1595.0,79.8);
                epaConversionTable.put(1605.0,83.4);
                epaConversionTable.put(1615.0,87.2);
                epaConversionTable.put(1630.0,92.7);
                epaConversionTable.put(1645.0,98.3);
                epaConversionTable.put(1660.0,104.1);
                epaConversionTable.put(1675.0,109.5);
                epaConversionTable.put(1690.0,115.2);
                epaConversionTable.put(1710.0,123.0);
                epaConversionTable.put(1735.0,132.1);
                epaConversionTable.put(1750.0,137.7);
                epaConversionTable.put(1760.0,141.5);
                epaConversionTable.put(1775.0,147.2);
                epaConversionTable.put(1810.0,160.2);
                epaConversionTable.put(1829.0,167.3);
                epaConversionTable.put(1844.0,172.9);
                epaConversionTable.put(1860.0,179.1);
                epaConversionTable.put(1878.0,185.9);
                epaConversionTable.put(1909.0,197.4);
                epaConversionTable.put(1931.0,205.9);
                epaConversionTable.put(1977.0,223.0);
                epaConversionTable.put(2055.0,252.2);
                epaConversionTable.put(2135.0,282.1);
                epaConversionTable.put(2267.0,331.7);
                epaConversionTable.put(2339.0,358.8);
                epaConversionTable.put(2400.0,383.6);
                epaConversionTable.put(2500.0,422.6);
                epaConversionTable.put(3000.0,617.8);
                epaConversionTable.put(4000.0,1008.0);
                
            }

    }
    
}
