package webProject.togetherPartyTonight.global.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class PointConverter implements AttributeConverter<Point,String> {
    @Override
    public String convertToDatabaseColumn(Point attribute) {
        return attribute.toString();
    }

    @Override
    public Point convertToEntityAttribute(String dbData) {
        GeometryFactory gf = new GeometryFactory();
        String coordinates = "";
        for (int i=0;i <dbData.length();i++) {
            if (dbData.charAt(i) == '(') {
                coordinates = dbData.substring(i+1, dbData.length()-1);
                break;
            }
        } // convertToEntityAttribute가 두번 동작헤서 ( 1. POINT(10 20) 2. POINT (10 20) )

        String[] split = coordinates.split(" ");

        double x = Double.parseDouble(split[0]);
        double y = Double.parseDouble(split[1]);

        Point point = gf.createPoint(new Coordinate(x,y));
        return point;
    }
}
