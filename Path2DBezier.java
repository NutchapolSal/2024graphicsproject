import java.awt.Point;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Path2DBezier extends Path2DData {
    public AnimPoint pNext;
    public List<AnimPoint> morePoints;

    Path2DBezier(AnimPoint pNext, AnimPoint... morePoints) {
        this(pNext, new ArrayList<>(Arrays.asList(morePoints)));
    }

    Path2DBezier(AnimPoint pNext, List<AnimPoint> morePoints) {
        this.pNext = pNext;
        this.morePoints = morePoints;
    }

    @Override
    public void run(Path2D path, double time) {
        path.curveTo(pNext.get(time).x, pNext.get(time).y, morePoints.get(0).get(time).x, morePoints.get(0).get(time).y,
                morePoints.get(1).get(time).x, morePoints.get(1).get(time).y);
        for (int i = 3; i < morePoints.size(); i += 2) {
            Point pA = morePoints.get(i - 2).get(time);
            Point pBtemp = morePoints.get(i - 3).get(time);
            Point pB = new Point(pA.x + (pA.x - pBtemp.x), pA.y + (pA.y - pBtemp.y));
            Point pC = morePoints.get(i - 1).get(time);
            Point pD = morePoints.get(i).get(time);

            path.curveTo(pB.x, pB.y, pC.x, pC.y, pD.x, pD.y);
        }
    }

    @Override
    public int size() {
        return morePoints.size() + 1;
    }

    @Override
    AnimPoint lastPoint() {
        return morePoints.get(morePoints.size() - 1);
    }

    @Override
    public String exportCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("new Path2DBezier(");
        sb.append(ImEx.exportCode(this.pNext));
        for (var point : this.morePoints) {
            sb.append(", ");
            sb.append(ImEx.exportCode(point));
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String exportString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BEZIER ");
        sb.append(ImEx.exportString(this.pNext));
        sb.append(" ");
        for (var point : this.morePoints) {
            sb.append(ImEx.exportString(point));
            sb.append(" ");
        }
        sb.append("END");
        return sb.toString();
    }

}