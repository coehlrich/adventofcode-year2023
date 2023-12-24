package com.coehlrich.adventofcode.day24;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;
import com.coehlrich.adventofcode.util.Point3;
import com.coehlrich.adventofcode.util.Point3Long;
import com.microsoft.z3.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        List<Hailstone> hailstones = input.lines().map(Hailstone::parse).toList();
        System.out.println(hailstones);
        int part1 = 0;
        long min = 200000000000000L;
        long max = 400000000000000L;
//        long min = 7;
//        long max = 27;
        for (int i = 0; i < hailstones.size(); i++) {
            Hailstone h1 = hailstones.get(i);
            for (int j = i + 1; j < hailstones.size(); j++) {
                Hailstone h2 = hailstones.get(j);
                // https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection#Given_two_points_on_each_line
                BigInteger x1 = BigInteger.valueOf(h1.pos().x());
                BigInteger y1 = BigInteger.valueOf(h1.pos().y());
                BigInteger x2 = x1.add(BigInteger.valueOf(h1.speed().x()));
                BigInteger y2 = y1.add(BigInteger.valueOf(h1.speed().y()));

                BigInteger x3 = BigInteger.valueOf(h2.pos().x());
                BigInteger y3 = BigInteger.valueOf(h2.pos().y());
                BigInteger x4 = x3.add(BigInteger.valueOf(h2.speed().x()));
                BigInteger y4 = y3.add(BigInteger.valueOf(h2.speed().y()));

                // (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4)
//                long testDemoninator = (x1.longValueExact() - x2.longValueExact()) * (y3.longValueExact() - y4.longValueExact()) - (y1.longValueExact() - y2.longValueExact()) * (x3.longValueExact() - x4.longValueExact());
                BigInteger demoninator = x1.subtract(x2).multiply(y3.subtract(y4)).subtract(y1.subtract(y2).multiply(x3.subtract(x4)));
//                System.out.println(demoninator);
                if (!demoninator.equals(BigInteger.ZERO)) {
                    // (x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4)
//                    long testX = (x1.longValueExact() * y2.longValueExact() - y1.longValueExact() * x2.longValueExact()) * (x3.longValueExact() - x4.longValueExact()) - (x1.longValueExact() - x2.longValueExact()) * (x3.longValueExact() * y4.longValueExact() - y3.longValueExact() * x4.longValueExact());
                    BigInteger topX = (x1.multiply(y2).subtract(y1.multiply(x2))).multiply(x3.subtract(x4)).subtract(x1.subtract(x2).multiply(x3.multiply(y4).subtract(y3.multiply(x4))));

                    // (x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4)
                    BigInteger topY = (x1.multiply(y2).subtract(y1.multiply(x2))).multiply(y3.subtract(y4)).subtract(y1.subtract(y2).multiply(x3.multiply(y4).subtract(y3.multiply(x4))));

                    long x = topX.divide(demoninator).longValueExact();
                    long y = topY.divide(demoninator).longValueExact();
//                    System.out.println(testDemoninator);
//                    System.out.println(demoninator);
//                    System.out.println(y);
                    if (sign(h1.speed().x()) == sign(x - h1.pos().x()) &&
                            sign(h1.speed().y()) == sign(y - h1.pos().y()) &&
                            sign(h2.speed().x()) == sign(x - h2.pos().x()) &&
                            sign(h2.speed().y()) == sign(y - h2.pos().y())) {
                        if (x >= min && x <= max && y >= min && y <= max) {
                            part1++;
                        } else {
//                            System.out.println(x);
//                            System.out.println(y);
                        }
                    } else {
//                        System.out.println(x);
//                        System.out.println(y);
                    }
                }
            }
        }

        Context ctx = new Context();
        IntExpr x = ctx.mkIntConst("x");
        IntExpr dx = ctx.mkIntConst("dx");
        IntExpr y = ctx.mkIntConst("y");
        IntExpr dy = ctx.mkIntConst("dy");
        IntExpr z = ctx.mkIntConst("z");
        IntExpr dz = ctx.mkIntConst("dz");
        IntExpr result = ctx.mkIntConst("result");

        Solver solver = ctx.mkSolver();
        List<IntExpr> tExpr = new ArrayList<>();
//        solver.add(ctx.mkGt(a, ctx.mkInt(0)));
        for (int i = 0; i < hailstones.size(); i++) {
            Hailstone hailstone = hailstones.get(i);
            IntExpr t = ctx.mkIntConst("t" + i);
            tExpr.add(t);
            long hx = hailstone.pos().x();
            int hdx = hailstone.speed().x();
            long hy = hailstone.pos().y();
            int hdy = hailstone.speed().y();
            long hz = hailstone.pos().z();
            int hdz = hailstone.speed().z();

            solver.add(ctx.mkEq(ctx.mkAdd(ctx.mkMul(dx, t), x), ctx.mkAdd(ctx.mkMul(ctx.mkInt(hdx), t), ctx.mkInt(hx))));
            solver.add(ctx.mkEq(ctx.mkAdd(ctx.mkMul(dy, t), y), ctx.mkAdd(ctx.mkMul(ctx.mkInt(hdy), t), ctx.mkInt(hy))));
            solver.add(ctx.mkEq(ctx.mkAdd(ctx.mkMul(dz, t), z), ctx.mkAdd(ctx.mkMul(ctx.mkInt(hdz), t), ctx.mkInt(hz))));
        }

//        for (int i = 0; i < tExpr.size(); i++) {
//            IntExpr ta = tExpr.get(i);
//            for (int j = i + 1; j < tExpr.size(); j++) {
//                IntExpr tb = tExpr.get(j);
////                solver.add(ctx.mkNot(ctx.mkEq(ta, tb)));
//            }
//        }

        solver.add(ctx.mkEq(result, ctx.mkAdd(x, y, z)));

        Status status = solver.check();
//        System.out.println(status);
        Model model = solver.getModel();
//        System.out.println(model.getConstInterp(x));
//        System.out.println(model.getConstInterp(dx));
//        System.out.println(model.getConstInterp(y));
//        System.out.println(model.getConstInterp(dy));
//        System.out.println(model.getConstInterp(z));
//        System.out.println(model.getConstInterp(dz));
//        for (IntExpr t : tExpr) {
//            System.out.println(model.getConstInterp(t));
//        }

        return new Result(part1, model.getConstInterp(result));
    }

    private long getValue(List<Hailstone> hailstones, ToLongFunction<Point3Long> coordinate, ToIntFunction<Point3> speed) {

        return 0;

    }

    private long sign(long value) {
        return value < 0 ? -1 : value > 0 ? 1 : 0;
    }

}
