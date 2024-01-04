import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main {
    public static void main(String[] args) {

        List<GraphicLayer> instructions = new ArrayList<>();
        instructions.add(new GraphicLayer("floodbg")
                .setShown(true)
                .add(new GraphicBezierCurve("#0d0d0d", 1, new Point(621, 358), new Point(524, 353), new Point(201, 477),
                        new Point(117, 611)))
                .add(new GraphicBezierCurve("#302c6d", 1, new Point(609, 218), new Point(475, 242), new Point(121, 398),
                        new Point(-6, 546)))
                .add(new GraphicBezierCurve("#6296ef", 1, new Point(605, 43), new Point(456, 76), new Point(88, 184),
                        new Point(-2, 354)))
                .add(new GraphicBezierCurve("#a7c7fe", 1, new Point(204, -2), new Point(98, 42), new Point(21, 100),
                        new Point(-7, 138)))
                .add(new GraphicFloodFill("#0d0d0d", new Point(454, 523)))
                .add(new GraphicFloodFill("#251b3c", new Point(256, 419)))
                .add(new GraphicFloodFill("#302c6d", new Point(215, 332)))
                .add(new GraphicFloodFill("#6296ef", new Point(125, 169)))
                .add(new GraphicFloodFill("#a7c7fe", new Point(19, 20))));
        instructions.add(new GraphicLayer("polygonal skies")
                .setShown(true)
                .add(new GraphicPolygon("#b7d8ff", new Point(88, -22), new Point(16, 38), new Point(119, 29)))
                .add(new GraphicPolygon("#77a3ef", new Point(382, 68), new Point(489, 19), new Point(429, -27)))
                .add(new GraphicPolygon("#79a4ef", new Point(510, 13), new Point(520, 44), new Point(613, 19)))
                .add(new GraphicPolygon("#383877", new Point(400, 184), new Point(509, 204), new Point(435, 124)))
                .add(new GraphicPolygon("#413575", new Point(496, 90), new Point(563, 196), new Point(562, 99)))
                .add(new GraphicPolygon("#3b3b7a", new Point(193, 204), new Point(268, 283), new Point(143, 275)))
                .add(new GraphicPolygon("#383877", new Point(45, 378), new Point(63, 467), new Point(-10, 468)))
                .add(new GraphicPolygon("#292144", new Point(34, 551), new Point(46, 588), new Point(6, 559)))
                .add(new GraphicPolygon("#292144", new Point(145, 551), new Point(189, 496), new Point(125, 468)))
                .add(new GraphicPolygon("#271f42", new Point(258, 370), new Point(254, 454), new Point(179, 450)))
                .add(new GraphicPolygon("#271f43", new Point(482, 355), new Point(384, 395), new Point(338, 344)))
                .add(new GraphicPolygon("#272044", new Point(603, 341), new Point(531, 334), new Point(575, 243)))
                .add(new GraphicPolygon("#111111", new Point(588, 437), new Point(491, 424), new Point(583, 369)))
                .add(new GraphicPolygon("#121212", new Point(422, 522), new Point(406, 448), new Point(506, 463)))
                .add(new GraphicPolygon("#111111", new Point(226, 563), new Point(355, 531), new Point(362, 580))));
        instructions.add(new GraphicLayer("star and snow")
                .setShown(true)
                .add(new GraphicPolyBezier("#ffffa4", 2, new Point(50, 19),
                        new PolyBezierData(new Point(49, 49), new Point(58, 42), new Point(68, 44)),
                        new PolyBezierData(new Point(58, 42), new Point(47, 67), new Point(52, 79)),
                        new PolyBezierData(new Point(55, 46), new Point(33, 52), new Point(29, 46)),
                        new PolyBezierData(new Point(33, 52), new Point(52, 34), new Point(50, 19))))
                .add(new GraphicFloodFill("#ffff42", new Point(48, 47)))
                .add(new GraphicPolyBezier("#ffffa4", 2, new Point(67, 77),
                        new PolyBezierData(new Point(65, 90), new Point(69, 89), new Point(74, 90)),
                        new PolyBezierData(new Point(69, 89), new Point(65, 90), new Point(66, 109)),
                        new PolyBezierData(new Point(65, 90), new Point(63, 89), new Point(56, 91)),
                        new PolyBezierData(new Point(63, 89), new Point(64, 84), new Point(67, 77))))
                .add(new GraphicFloodFill("#ffff42", new Point(64, 87)))
                .add(new GraphicPolyBezier("#ffffa4", 2, new Point(551, 15),
                        new PolyBezierData(new Point(548, 22), new Point(531, 40), new Point(550, 50)),
                        new PolyBezierData(new Point(531, 42), new Point(522, 67), new Point(522, 63)),
                        new PolyBezierData(new Point(531, 42), new Point(520, 31), new Point(518, 32)),
                        new PolyBezierData(new Point(520, 31), new Point(533, 37), new Point(551, 15))))
                .add(new GraphicFloodFill("#ffff42", new Point(533, 40)))
                .add(new GraphicPolyBezier("#ffffa4", 2, new Point(517, 154),
                        new PolyBezierData(new Point(517, 156), new Point(503, 172), new Point(518, 176)),
                        new PolyBezierData(new Point(511, 173), new Point(498, 182), new Point(498, 192)),
                        new PolyBezierData(new Point(499, 191), new Point(503, 172), new Point(494, 168)),
                        new PolyBezierData(new Point(505, 167), new Point(504, 172), new Point(517, 154))))
                .add(new GraphicFloodFill("#ffff42", new Point(505, 172)))
                .add(new GraphicLine("#dbecff", 2, new Point(578, 78), new Point(537, 152)))
                .add(new GraphicLine("#dbecff", 2, new Point(542, 79), new Point(581, 150)))
                .add(new GraphicLine("#ffffff", 2, new Point(559, 91), new Point(562, 141)))
                .add(new GraphicLine("#dbecff", 2, new Point(592, 106), new Point(523, 118)))
                .add(new GraphicLine("#ffffff", 2, new Point(575, 99), new Point(540, 126)))
                .add(new GraphicLine("#ffffff", 2, new Point(543, 101), new Point(582, 125)))
                .add(new GraphicBezierCurve("#ffffff", 2, new Point(551, 84), new Point(548, 80), new Point(533, 67),
                        new Point(543, 90)))
                .add(new GraphicBezierCurve("#ffffff", 2, new Point(554, 92), new Point(546, 87), new Point(546, 92),
                        new Point(548, 97)))
                .add(new GraphicBezierCurve("#ffffff", 2, new Point(563, 97), new Point(562, 87), new Point(558, 85),
                        new Point(555, 97)))
                .add(new GraphicBezierCurve("#ffffff", 2, new Point(578, 87), new Point(583, 66), new Point(575, 80),
                        new Point(569, 83)))
                .add(new GraphicBezierCurve("#ffffff", 1, new Point(575, 94), new Point(571, 87), new Point(578, 82),
                        new Point(566, 90)))
                .add(new GraphicBezierCurve("#ffffff", 2, new Point(588, 111), new Point(600, 107), new Point(593, 104),
                        new Point(590, 100)))
                .add(new GraphicBezierCurve("#ffffff", 2, new Point(581, 112), new Point(583, 112), new Point(590, 105),
                        new Point(583, 103)))
                .add(new GraphicBezierCurve("#ffffff", 2, new Point(572, 143), new Point(583, 156), new Point(580, 149),
                        new Point(583, 140)))
                .add(new GraphicBezierCurve("#ffffff", 2, new Point(581, 119), new Point(591, 129), new Point(569, 124),
                        new Point(576, 126)))
                .add(new GraphicBezierCurve("#ffffff", 2, new Point(556, 134), new Point(565, 148), new Point(562, 140),
                        new Point(567, 133)))
                .add(new GraphicBezierCurve("#ffffff", 2, new Point(548, 146), new Point(533, 154), new Point(537, 156),
                        new Point(536, 143)))
                .add(new GraphicBezierCurve("#ffffff", 2, new Point(552, 137), new Point(541, 143), new Point(542, 145),
                        new Point(542, 132)))
                .add(new GraphicBezierCurve("#ffffff", 2, new Point(541, 110), new Point(539, 111), new Point(524, 117),
                        new Point(541, 119)))
                .add(new GraphicBezierCurve("#ffffff", 2, new Point(530, 111), new Point(520, 120), new Point(518, 116),
                        new Point(531, 122)))
                .add(new GraphicBezierCurve("#ffffff", 2, new Point(549, 100), new Point(542, 98), new Point(538, 103),
                        new Point(545, 107)))
                .add(new GraphicPolyline("#8282e5", 2, true, new Point(572, 181), new Point(588, 185),
                        new Point(570, 190), new Point(584, 177), new Point(580, 194)))
                .add(new GraphicPolyline("#8282e5", 2, true, new Point(425, 159), new Point(427, 169),
                        new Point(419, 164), new Point(430, 162), new Point(421, 170)))
                .add(new GraphicPolyline("#d1e7ff", 2, true, new Point(15, 110), new Point(21, 129), new Point(8, 119),
                        new Point(24, 117), new Point(11, 132)))
                .add(new GraphicPolyline("#514987", 2, true, new Point(162, 438), new Point(167, 446),
                        new Point(157, 442), new Point(168, 438), new Point(159, 449)))
                .add(new GraphicPolyline("#514987", 2, true, new Point(31, 569), new Point(44, 583), new Point(24, 576),
                        new Point(42, 570), new Point(28, 588))));
        instructions.add(new GraphicLayer("dirt")
                .setShown(true)
                .add(new GraphicBezierCurve("#000000", 2, new Point(341, 600), new Point(341, 539), new Point(489, 419),
                        new Point(600, 475)))
                .add(new GraphicFloodFill("#43281d", new Point(447, 492))));
        instructions.add(new GraphicLayer("christmas tree 3")
                .setShown(true)
                .add(new GraphicPolyBezier("#0a1d0b", 2, new Point(545, 395),
                        new PolyBezierData(new Point(537, 416), new Point(514, 445), new Point(495, 455)),
                        new PolyBezierData(new Point(511, 446), new Point(527, 446), new Point(533, 462)),
                        new PolyBezierData(new Point(547, 451), new Point(558, 447), new Point(573, 458)),
                        new PolyBezierData(new Point(579, 448), new Point(586, 442), new Point(600, 439)),
                        new PolyBezierData(new Point(584, 442), new Point(556, 419), new Point(550, 395))))
                .add(new GraphicPolyline("#ffdd1e", 2, true, new Point(548, 391), new Point(536, 399),
                        new Point(542, 387), new Point(533, 380), new Point(546, 381), new Point(549, 369),
                        new Point(553, 381), new Point(564, 382), new Point(554, 387), new Point(559, 400)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(570, 450), new Point(565, 443), new Point(557, 429),
                        new Point(553, 416)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(536, 455), new Point(539, 448), new Point(541, 432),
                        new Point(543, 418)))
                .add(new GraphicPolyBezier("#0a1d0b", 2, new Point(522, 450),
                        new PolyBezierData(new Point(514, 468), new Point(503, 486), new Point(486, 500)),
                        new PolyBezierData(new Point(515, 494), new Point(522, 501), new Point(531, 508)),
                        new PolyBezierData(new Point(537, 504), new Point(549, 503), new Point(559, 509)),
                        new PolyBezierData(new Point(569, 497), new Point(581, 490), new Point(595, 503)),
                        new PolyBezierData(new Point(588, 499), new Point(601, 481), new Point(613, 484)),
                        new PolyBezierData(new Point(594, 483), new Point(590, 462), new Point(581, 448))))
                .add(new GraphicPolyline("#000000", 2, false, new Point(540, 506), new Point(554, 578),
                        new Point(584, 567), new Point(567, 500)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(531, 497), new Point(535, 490), new Point(537, 474),
                        new Point(538, 461)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(559, 500), new Point(555, 486), new Point(554, 475),
                        new Point(552, 458)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(586, 493), new Point(578, 483), new Point(572, 470),
                        new Point(571, 464)))
                .add(new GraphicCircle("#ff1b00", 2, new Point(497, 460), 6))
                .add(new GraphicFloodFill("#304d31", new Point(535, 417)))
                .add(new GraphicFloodFill("#304d31", new Point(544, 463)))
                .add(new GraphicFloodFill("#000000", new Point(550, 511)))
                .add(new GraphicFloodFill("#901c0d", new Point(497, 460)))
                .add(new GraphicFloodFill("#d09e07", new Point(550, 386)))
                .add(new GraphicCircle("#ffdd1e", 2, new Point(529, 466), 6))
                .add(new GraphicCircle("#ff1b00", 2, new Point(577, 463), 6))
                .add(new GraphicCircle("#ff1b00", 2, new Point(528, 514), 6))
                .add(new GraphicFloodFill("#d09e07", new Point(529, 464)))
                .add(new GraphicFloodFill("#901c0d", new Point(578, 462)))
                .add(new GraphicFloodFill("#901c0d", new Point(530, 513)))
                .add(new GraphicCircle("#ffdd1e", 2, new Point(598, 508), 6))
                .add(new GraphicFloodFill("#d09e07", new Point(594, 507)))
                .add(new GraphicLine("#95b130", 3, new Point(561, 423), new Point(554, 414)))
                .add(new GraphicLine("#95b130", 3, new Point(571, 437), new Point(577, 442)))
                .add(new GraphicLine("#95b130", 2, new Point(569, 440), new Point(573, 446)))
                .add(new GraphicLine("#95b130", 2, new Point(549, 418), new Point(552, 430)))
                .add(new GraphicLine("#95b130", 2, new Point(546, 418), new Point(546, 429)))
                .add(new GraphicLine("#95b130", 2, new Point(538, 418), new Point(537, 427)))
                .add(new GraphicLine("#95b130", 2, new Point(533, 424), new Point(531, 428)))
                .add(new GraphicLine("#95b130", 2, new Point(528, 431), new Point(520, 440)))
                .add(new GraphicLine("#95b130", 2, new Point(530, 437), new Point(526, 441)))
                .add(new GraphicLine("#95b130", 2, new Point(534, 437), new Point(532, 442)))
                .add(new GraphicLine("#95b130", 2, new Point(544, 445), new Point(545, 449)))
                .add(new GraphicLine("#95b130", 2, new Point(548, 440), new Point(551, 446)))
                .add(new GraphicLine("#95b130", 2, new Point(554, 442), new Point(558, 447)))
                .add(new GraphicLine("#95b130", 3, new Point(566, 471), new Point(572, 485)))
                .add(new GraphicLine("#95b130", 2, new Point(582, 476), new Point(587, 482)))
                .add(new GraphicLine("#95b130", 2, new Point(584, 472), new Point(593, 480)))
                .add(new GraphicLine("#95b130", 2, new Point(540, 485), new Point(538, 493)))
                .add(new GraphicLine("#95b130", 2, new Point(543, 481), new Point(543, 492)))
                .add(new GraphicLine("#95b130", 2, new Point(547, 482), new Point(548, 491)))
                .add(new GraphicLine("#95b130", 2, new Point(552, 488), new Point(552, 491)))
                .add(new GraphicLine("#95b130", 2, new Point(531, 475), new Point(531, 480)))
                .add(new GraphicLine("#95b130", 2, new Point(521, 476), new Point(519, 482)))
                .add(new GraphicLine("#95b130", 2, new Point(527, 475), new Point(525, 480)))
                .add(new GraphicLine("#95b130", 2, new Point(520, 470), new Point(515, 477)))
                .add(new GraphicLine("#95b130", 2, new Point(563, 478), new Point(568, 487))));
        instructions.add(new GraphicLayer("christmas tree 2")
                .setShown(true)
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(480, 464), new Point(477, 485), new Point(471, 492),
                        new Point(461, 504)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(480, 463), new Point(488, 473), new Point(494, 485),
                        new Point(513, 488)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(461, 504), new Point(463, 499), new Point(481, 504),
                        new Point(478, 510)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(478, 508), new Point(490, 500), new Point(490, 498),
                        new Point(502, 503)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(503, 503), new Point(497, 497), new Point(512, 492),
                        new Point(512, 496)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(513, 496), new Point(508, 494), new Point(516, 484),
                        new Point(514, 489)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(472, 504), new Point(476, 515), new Point(470, 520),
                        new Point(459, 523)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(459, 535), new Point(470, 531), new Point(463, 522),
                        new Point(461, 524)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(479, 539), new Point(476, 533), new Point(469, 533),
                        new Point(461, 534)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(497, 534), new Point(492, 527), new Point(481, 531),
                        new Point(479, 538)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(512, 526), new Point(507, 527), new Point(505, 525),
                        new Point(496, 533)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(525, 517), new Point(524, 516), new Point(516, 513),
                        new Point(513, 526)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(528, 508), new Point(527, 509), new Point(525, 510),
                        new Point(525, 516)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(510, 496), new Point(513, 508), new Point(520, 508),
                        new Point(526, 508)))
                .add(new GraphicLine("#000000", 2, new Point(492, 532), new Point(500, 562)))
                .add(new GraphicLine("#000000", 2, new Point(510, 528), new Point(519, 554)))
                .add(new GraphicLine("#000000", 2, new Point(501, 560), new Point(519, 553)))
                .add(new GraphicBezierCurve("#ffdd1e", 2, new Point(480, 463), new Point(470, 461), new Point(479, 453),
                        new Point(480, 454), new Point(488, 463), new Point(477, 462)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(477, 501), new Point(481, 500), new Point(480, 484),
                        new Point(481, 480)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(498, 498), new Point(495, 493), new Point(490, 488),
                        new Point(488, 482)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(461, 535), new Point(472, 531), new Point(472, 523),
                        new Point(474, 520)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(479, 539), new Point(479, 533), new Point(483, 520),
                        new Point(482, 519)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(495, 515), new Point(497, 523), new Point(496, 528),
                        new Point(498, 532)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(505, 514), new Point(510, 520), new Point(510, 520),
                        new Point(514, 521)))
                .add(new GraphicFloodFill("#d09e07", new Point(478, 458)))
                .add(new GraphicFloodFill("#304d31", new Point(480, 468)))
                .add(new GraphicFloodFill("#304d31", new Point(508, 499)))
                .add(new GraphicFloodFill("#000000", new Point(507, 546)))
                .add(new GraphicCircle("#2a0909", 2, new Point(457, 506), 3))
                .add(new GraphicCircle("#ffdd1e", 2, new Point(478, 513), 3))
                .add(new GraphicCircle("#ff1b00", 2, new Point(505, 506), 3))
                .add(new GraphicCircle("#ffdd1e", 2, new Point(519, 489), 3))
                .add(new GraphicCircle("#3f3402", 2, new Point(457, 539), 3))
                .add(new GraphicCircle("#ff1b00", 2, new Point(479, 543), 3))
                .add(new GraphicCircle("#ff1b00", 2, new Point(455, 523), 3))
                .add(new GraphicCircle("#2a0909", 2, new Point(517, 527), 3))
                .add(new GraphicCircle("#ffdd1e", 2, new Point(527, 520), 3))
                .add(new GraphicCircle("#3f3402", 2, new Point(501, 538), 3))
                .add(new GraphicCircle("#2a0909", 2, new Point(533, 507), 3))
                .add(new GraphicFloodFill("#d09e07", new Point(458, 539)))
                .add(new GraphicFloodFill("#d09e07", new Point(526, 520)))
                .add(new GraphicFloodFill("#d09e07", new Point(477, 511)))
                .add(new GraphicFloodFill("#d09e07", new Point(519, 488)))
                .add(new GraphicFloodFill("#911c0d", new Point(458, 505)))
                .add(new GraphicFloodFill("#911c0d", new Point(505, 505)))
                .add(new GraphicFloodFill("#911c0d", new Point(454, 521)))
                .add(new GraphicFloodFill("#911c0d", new Point(478, 543)))
                .add(new GraphicFloodFill("#911c0d", new Point(518, 527)))
                .add(new GraphicFloodFill("#911c0d", new Point(533, 507)))
                .add(new GraphicFloodFill("#d09e07", new Point(500, 537)))
                .add(new GraphicLine("#95b130", 2, new Point(502, 488), new Point(508, 491)))
                .add(new GraphicLine("#95b130", 2, new Point(499, 489), new Point(501, 494)))
                .add(new GraphicLine("#95b130", 2, new Point(489, 493), new Point(493, 498)))
                .add(new GraphicLine("#95b130", 2, new Point(487, 493), new Point(487, 498)))
                .add(new GraphicLine("#95b130", 2, new Point(473, 496), new Point(471, 500)))
                .add(new GraphicLine("#95b130", 2, new Point(476, 496), new Point(474, 500)))
                .add(new GraphicLine("#95b130", 2, new Point(515, 510), new Point(520, 511)))
                .add(new GraphicLine("#95b130", 2, new Point(511, 514), new Point(515, 517)))
                .add(new GraphicLine("#95b130", 2, new Point(505, 518), new Point(509, 523)))
                .add(new GraphicLine("#95b130", 2, new Point(502, 520), new Point(505, 524)))
                .add(new GraphicLine("#95b130", 2, new Point(498, 518), new Point(501, 525)))
                .add(new GraphicLine("#95b130", 2, new Point(493, 522), new Point(493, 527)))
                .add(new GraphicLine("#95b130", 3, new Point(489, 522), new Point(489, 527)))
                .add(new GraphicLine("#95b130", 2, new Point(486, 521), new Point(484, 528)))
                .add(new GraphicLine("#95b130", 2, new Point(480, 528), new Point(477, 532)))
                .add(new GraphicLine("#95b130", 2, new Point(476, 528), new Point(472, 534)))
                .add(new GraphicLine("#95b130", 2, new Point(470, 522), new Point(467, 526))));
        instructions.add(new GraphicLayer("christmas tree")
                .setShown(true)
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(394, 534), new Point(396, 568), new Point(384, 560),
                        new Point(379, 568)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(390, 583), new Point(394, 572), new Point(386, 570),
                        new Point(379, 568)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(390, 583), new Point(389, 582), new Point(407, 563),
                        new Point(420, 581)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(420, 581), new Point(409, 569), new Point(401, 570),
                        new Point(390, 583)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(420, 581), new Point(420, 563), new Point(430, 559),
                        new Point(437, 562)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(447, 547), new Point(436, 544), new Point(430, 559),
                        new Point(437, 562)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(447, 547), new Point(438, 546), new Point(441, 541),
                        new Point(440, 538)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(403, 528), new Point(421, 538), new Point(430, 542),
                        new Point(440, 538)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(395, 600), new Point(397, 587), new Point(402, 588),
                        new Point(400, 576)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(395, 600), new Point(396, 595), new Point(402, 590),
                        new Point(414, 600)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(434, 593), new Point(420, 583), new Point(406, 614),
                        new Point(414, 600)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(434, 593), new Point(440, 579), new Point(456, 578),
                        new Point(452, 581)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(452, 581), new Point(452, 569), new Point(460, 570),
                        new Point(459, 563)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(438, 551), new Point(448, 562), new Point(448, 562),
                        new Point(459, 561)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(454, 574), new Point(472, 582), new Point(467, 578),
                        new Point(482, 575)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(479, 600), new Point(477, 606), new Point(467, 578),
                        new Point(482, 575)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(399, 533), new Point(400, 560), new Point(400, 530),
                        new Point(397, 561)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(399, 533), new Point(405, 551), new Point(402, 528),
                        new Point(408, 558)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(399, 533), new Point(414, 547), new Point(415, 544),
                        new Point(427, 546)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(410, 573), new Point(414, 596), new Point(412, 582),
                        new Point(412, 594)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(425, 566), new Point(426, 575), new Point(426, 575),
                        new Point(432, 578)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(452, 584), new Point(472, 598), new Point(453, 587),
                        new Point(477, 600)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(438, 591), new Point(436, 608), new Point(480, 612),
                        new Point(436, 600)))
                .add(new GraphicBezierCurve("#ff1b00", 2, new Point(403, 528), new Point(403, 527), new Point(403, 531),
                        new Point(394, 534)))
                .add(new GraphicBezierCurve("#ff1b00", 2, new Point(394, 534), new Point(380, 528), new Point(409, 506),
                        new Point(403, 528)))
                .add(new GraphicFloodFill("#901c0d", new Point(396, 524)))
                .add(new GraphicFloodFill("#304d31", new Point(402, 542)))
                .add(new GraphicFloodFill("#304d31", new Point(437, 571)))
                .add(new GraphicFloodFill("#304d31", new Point(437, 571)))
                .add(new GraphicFloodFill("#304d31", new Point(430, 596)))
                .add(new GraphicFloodFill("#304d31", new Point(401, 597)))
                .add(new GraphicFloodFill("#304d31", new Point(449, 589)))
                .add(new GraphicLine("#95b130", 3, new Point(431, 543), new Point(438, 544)))
                .add(new GraphicLine("#95b130", 2, new Point(427, 546), new Point(434, 548)))
                .add(new GraphicLine("#95b130", 2, new Point(423, 552), new Point(430, 558)))
                .add(new GraphicLine("#95b130", 2, new Point(420, 556), new Point(426, 558)))
                .add(new GraphicLine("#95b130", 2, new Point(419, 558), new Point(421, 562)))
                .add(new GraphicLine("#95b130", 2, new Point(417, 564), new Point(420, 573)))
                .add(new GraphicLine("#95b130", 2, new Point(412, 563), new Point(414, 572)))
                .add(new GraphicLine("#95b130", 2, new Point(408, 566), new Point(408, 571)))
                .add(new GraphicLine("#95b130", 2, new Point(404, 565), new Point(405, 569)))
                .add(new GraphicLine("#95b130", 2, new Point(399, 566), new Point(399, 571)))
                .add(new GraphicLine("#95b130", 3, new Point(394, 566), new Point(394, 571)))
                .add(new GraphicLine("#95b130", 2, new Point(390, 565), new Point(389, 570)))
                .add(new GraphicLine("#95b130", 3, new Point(446, 564), new Point(454, 566)))
                .add(new GraphicLine("#95b130", 3, new Point(445, 568), new Point(450, 573)))
                .add(new GraphicLine("#95b130", 3, new Point(441, 572), new Point(447, 575)))
                .add(new GraphicLine("#95b130", 2, new Point(439, 576), new Point(443, 580)))
                .add(new GraphicLine("#95b130", 2, new Point(435, 578), new Point(439, 584)))
                .add(new GraphicLine("#95b130", 2, new Point(430, 583), new Point(433, 589)))
                .add(new GraphicLine("#95b130", 2, new Point(425, 583), new Point(428, 588)))
                .add(new GraphicLine("#95b130", 2, new Point(423, 587), new Point(423, 588)))
                .add(new GraphicLine("#95b130", 2, new Point(418, 588), new Point(420, 592)))
                .add(new GraphicLine("#95b130", 2, new Point(413, 587), new Point(415, 593)))
                .add(new GraphicLine("#95b130", 3, new Point(408, 584), new Point(409, 594)))
                .add(new GraphicLine("#95b130", 3, new Point(404, 587), new Point(404, 590)))
                .add(new GraphicLine("#95b130", 3, new Point(451, 594), new Point(455, 598)))
                .add(new GraphicLine("#95b130", 3, new Point(459, 593), new Point(473, 602)))
                .add(new GraphicLine("#95b130", 2, new Point(466, 587), new Point(472, 592)))
                .add(new GraphicLine("#95b130", 2, new Point(467, 583), new Point(474, 587)))
                .add(new GraphicLine("#95b130", 2, new Point(469, 583), new Point(478, 584)))
                .add(new GraphicCircle("#ffdd1e", 2, new Point(375, 570), 3))
                .add(new GraphicCircle("#ff1b00", 2, new Point(389, 586), 3))
                .add(new GraphicCircle("#3f3402", 2, new Point(421, 583), 3))
                .add(new GraphicCircle("#3f3402", 2, new Point(450, 547), 3))
                .add(new GraphicCircle("#ff1b00", 2, new Point(439, 565), 3))
                .add(new GraphicCircle("#2a0909", 2, new Point(464, 561), 3))
                .add(new GraphicCircle("#ffdd1e", 2, new Point(455, 582), 3))
                .add(new GraphicCircle("#3f3402", 2, new Point(486, 574), 3))
                .add(new GraphicCircle("#ff1b00", 2, new Point(434, 597), 3))
                .add(new GraphicFloodFill("#d09e07", new Point(486, 575)))
                .add(new GraphicFloodFill("#d09e07", new Point(454, 582)))
                .add(new GraphicFloodFill("#d09e07", new Point(450, 545)))
                .add(new GraphicFloodFill("#d09e07", new Point(419, 582)))
                .add(new GraphicFloodFill("#d09e07", new Point(374, 570)))
                .add(new GraphicFloodFill("#901c0d", new Point(389, 587)))
                .add(new GraphicFloodFill("#901c0d", new Point(438, 565)))
                .add(new GraphicFloodFill("#901c0d", new Point(465, 560)))
                .add(new GraphicFloodFill("#901c0d", new Point(433, 596))));
        instructions.add(new GraphicLayer("christmas tree 5")
                .setShown(true)
                .add(new GraphicPolyBezier("#0a1d0b", 2, new Point(510, 533),
                        new PolyBezierData(new Point(509, 541), new Point(510, 553), new Point(498, 556)),
                        new PolyBezierData(new Point(502, 559), new Point(499, 565), new Point(494, 567)),
                        new PolyBezierData(new Point(500, 569), new Point(504, 572), new Point(504, 577)),
                        new PolyBezierData(new Point(510, 573), new Point(516, 570), new Point(523, 575)),
                        new PolyBezierData(new Point(529, 571), new Point(531, 565), new Point(538, 566)),
                        new PolyBezierData(new Point(537, 561), new Point(535, 555), new Point(539, 549)),
                        new PolyBezierData(new Point(523, 551), new Point(516, 543), new Point(510, 532))))
                .add(new GraphicPolyBezier("#0a1d0b", 2, new Point(501, 571),
                        new PolyBezierData(new Point(500, 579), new Point(492, 585), new Point(486, 591)),
                        new PolyBezierData(new Point(497, 591), new Point(502, 594), new Point(505, 598)),
                        new PolyBezierData(new Point(512, 596), new Point(519, 594), new Point(525, 599)),
                        new PolyBezierData(new Point(528, 592), new Point(534, 590), new Point(542, 590)),
                        new PolyBezierData(new Point(542, 581), new Point(549, 571), new Point(559, 569)),
                        new PolyBezierData(new Point(550, 568), new Point(542, 565), new Point(537, 561))))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(507, 556), new Point(508, 559), new Point(503, 565),
                        new Point(497, 569)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(513, 555), new Point(513, 564), new Point(510, 569),
                        new Point(507, 575)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(518, 553), new Point(521, 560), new Point(522, 567),
                        new Point(523, 575)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(523, 553), new Point(525, 558), new Point(531, 562),
                        new Point(537, 566)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(533, 577), new Point(535, 581), new Point(537, 585),
                        new Point(541, 589)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(518, 578), new Point(521, 584), new Point(523, 588),
                        new Point(524, 593)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(509, 578), new Point(509, 584), new Point(506, 589),
                        new Point(505, 593)))
                .add(new GraphicPolyline("#000000", 2, false, new Point(535, 590), new Point(540, 605),
                        new Point(519, 608), new Point(516, 595)))
                .add(new GraphicCircle("#ffdd1e", 3, new Point(507, 531), 3))
                .add(new GraphicCircle("#2a0909", 2, new Point(542, 550), 2))
                .add(new GraphicCircle("#ffdd1e", 2, new Point(539, 568), 2))
                .add(new GraphicCircle("#ff1b00", 2, new Point(524, 578), 2))
                .add(new GraphicCircle("#3f3402", 2, new Point(503, 579), 2))
                .add(new GraphicCircle("#ff1b00", 2, new Point(493, 569), 2))
                .add(new GraphicCircle("#3f3402", 2, new Point(496, 558), 2))
                .add(new GraphicCircle("#ffdd1e", 2, new Point(561, 573), 3))
                .add(new GraphicCircle("#2a0909", 2, new Point(544, 592), 3))
                .add(new GraphicCircle("#3f3402", 2, new Point(526, 603), 3))
                .add(new GraphicCircle("#ff1b00", 2, new Point(505, 600), 3))
                .add(new GraphicCircle("#ffdd1e", 2, new Point(486, 594), 3))
                .add(new GraphicFloodFill("#304d31", new Point(523, 559)))
                .add(new GraphicFloodFill("#304d31", new Point(529, 582)))
                .add(new GraphicFloodFill("#000000", new Point(527, 598)))
                .add(new GraphicFloodFill("#000000", new Point(518, 598)))
                .add(new GraphicFloodFill("#d09e07", new Point(485, 592)))
                .add(new GraphicFloodFill("#911c0d", new Point(545, 591)))
                .add(new GraphicFloodFill("#911c0d", new Point(504, 598)))
                .add(new GraphicFloodFill("#d09e07", new Point(561, 572)))
                .add(new GraphicFloodFill("#d09e07", new Point(507, 530)))
                .add(new GraphicLine("#95b130", 3, new Point(530, 554), new Point(536, 558)))
                .add(new GraphicLine("#95b130", 2, new Point(526, 561), new Point(530, 567)))
                .add(new GraphicLine("#95b130", 2, new Point(516, 563), new Point(518, 569)))
                .add(new GraphicLine("#95b130", 2, new Point(514, 568), new Point(515, 571)))
                .add(new GraphicLine("#95b130", 2, new Point(504, 558), new Point(503, 562)))
                .add(new GraphicLine("#95b130", 2, new Point(507, 562), new Point(501, 567)))
                .add(new GraphicLine("#95b130", 2, new Point(508, 566), new Point(504, 574)))
                .add(new GraphicLine("#95b130", 2, new Point(506, 586), new Point(502, 594)))
                .add(new GraphicLine("#95b130", 2, new Point(502, 587), new Point(498, 591)))
                .add(new GraphicLine("#95b130", 2, new Point(497, 587), new Point(494, 591)))
                .add(new GraphicLine("#95b130", 2, new Point(514, 591), new Point(508, 595)))
                .add(new GraphicLine("#95b130", 2, new Point(515, 590), new Point(514, 595)))
                .add(new GraphicLine("#95b130", 2, new Point(516, 589), new Point(517, 596)))
                .add(new GraphicLine("#95b130", 2, new Point(519, 588), new Point(521, 596)))
                .add(new GraphicLine("#95b130", 2, new Point(527, 585), new Point(531, 591)))
                .add(new GraphicLine("#95b130", 2, new Point(529, 581), new Point(534, 589)))
                .add(new GraphicLine("#95b130", 2, new Point(535, 574), new Point(540, 579)))
                .add(new GraphicLine("#95b130", 2, new Point(539, 572), new Point(542, 576)))
                .add(new GraphicLine("#95b130", 2, new Point(542, 569), new Point(545, 572)))
                .add(new GraphicLine("#95b130", 2, new Point(543, 567), new Point(546, 570))));
        instructions.add(new GraphicLayer("christmas tree 4")
                .setShown(true)
                .add(new GraphicPolyBezier("#0a1d0b", 2, new Point(558, 485),
                        new PolyBezierData(new Point(557, 500), new Point(555, 528), new Point(544, 536)),
                        new PolyBezierData(new Point(558, 534), new Point(561, 538), new Point(566, 544)),
                        new PolyBezierData(new Point(573, 530), new Point(578, 533), new Point(585, 534)),
                        new PolyBezierData(new Point(586, 529), new Point(584, 522), new Point(589, 517)),
                        new PolyBezierData(new Point(578, 513), new Point(568, 500), new Point(558, 483))))
                .add(new GraphicPolyBezier("#0a1d0b", 2, new Point(558, 537),
                        new PolyBezierData(new Point(557, 553), new Point(550, 567), new Point(544, 580)),
                        new PolyBezierData(new Point(559, 576), new Point(564, 579), new Point(571, 584)),
                        new PolyBezierData(new Point(579, 571), new Point(589, 575), new Point(597, 577)),
                        new PolyBezierData(new Point(597, 568), new Point(597, 558), new Point(614, 557)),
                        new PolyBezierData(new Point(593, 546), new Point(592, 541), new Point(586, 531))))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(562, 507), new Point(563, 517), new Point(563, 524),
                        new Point(561, 533)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(567, 507), new Point(569, 514), new Point(572, 518),
                        new Point(578, 525)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(568, 540), new Point(569, 552), new Point(569, 561),
                        new Point(567, 571)))
                .add(new GraphicBezierCurve("#0a1d0b", 2, new Point(574, 539), new Point(578, 549), new Point(583, 558),
                        new Point(589, 566)))
                .add(new GraphicPolyline("#000000", 2, false, new Point(576, 580), new Point(581, 601),
                        new Point(592, 597), new Point(585, 575)))
                .add(new GraphicCircle("#ffdd1e", 2, new Point(558, 479), 4))
                .add(new GraphicCircle("#3f3402", 2, new Point(593, 517), 3))
                .add(new GraphicCircle("#ff1b00", 2, new Point(585, 538), 3))
                .add(new GraphicCircle("#ffdd1e", 2, new Point(564, 546), 3))
                .add(new GraphicCircle("#2a0909", 2, new Point(540, 539), 3))
                .add(new GraphicCircle("#3f3402", 2, new Point(543, 583), 4))
                .add(new GraphicCircle("#ff1b00", 2, new Point(571, 589), 4))
                .add(new GraphicCircle("#3f3402", 2, new Point(597, 581), 4))
                .add(new GraphicFloodFill("#d09e07", new Point(559, 478)))
                .add(new GraphicFloodFill("#d09e07", new Point(563, 544)))
                .add(new GraphicFloodFill("#d09e07", new Point(543, 583)))
                .add(new GraphicFloodFill("#d09e07", new Point(595, 579)))
                .add(new GraphicFloodFill("#d09e07", new Point(593, 517)))
                .add(new GraphicFloodFill("#911c0d", new Point(541, 539)))
                .add(new GraphicFloodFill("#911c0d", new Point(584, 538)))
                .add(new GraphicFloodFill("#911c0d", new Point(571, 587)))
                .add(new GraphicFloodFill("#304d31", new Point(568, 522)))
                .add(new GraphicFloodFill("#304d31", new Point(577, 538)))
                .add(new GraphicFloodFill("#000000", new Point(581, 577)))
                .add(new GraphicFloodFill("#d09e07", new Point(558, 477)))
                .add(new GraphicLine("#95b130", 2, new Point(575, 516), new Point(581, 524)))
                .add(new GraphicLine("#95b130", 2, new Point(575, 514), new Point(582, 520)))
                .add(new GraphicLine("#95b130", 2, new Point(569, 522), new Point(576, 528)))
                .add(new GraphicLine("#95b130", 2, new Point(565, 523), new Point(569, 529)))
                .add(new GraphicLine("#95b130", 2, new Point(559, 522), new Point(559, 531)))
                .add(new GraphicLine("#95b130", 3, new Point(555, 521), new Point(554, 529)))
                .add(new GraphicLine("#95b130", 4, new Point(592, 553), new Point(600, 556)))
                .add(new GraphicLine("#95b130", 3, new Point(589, 558), new Point(594, 562)))
                .add(new GraphicLine("#95b130", 2, new Point(581, 560), new Point(587, 570)))
                .add(new GraphicLine("#95b130", 2, new Point(577, 563), new Point(581, 571)))
                .add(new GraphicLine("#95b130", 2, new Point(573, 566), new Point(577, 572)))
                .add(new GraphicLine("#95b130", 2, new Point(571, 569), new Point(572, 575)))
                .add(new GraphicLine("#95b130", 3, new Point(563, 563), new Point(563, 574)))
                .add(new GraphicLine("#95b130", 2, new Point(558, 566), new Point(558, 572)))
                .add(new GraphicLine("#95b130", 2, new Point(555, 568), new Point(553, 574))));
        instructions.add(new GraphicLayer("santa")
                .setShown(true)
                .add(new GraphicBezierCurve("#440e12", 2, new Point(66, 449), new Point(32, 413), new Point(0, 488),
                        new Point(20, 507)))
                .add(new GraphicBezierCurve("#cccccc", 2, new Point(20, 507), new Point(23, 502), new Point(29, 500),
                        new Point(32, 506)))
                .add(new GraphicBezierCurve("#cccccc", 2, new Point(20, 507), new Point(12, 517), new Point(26, 520),
                        new Point(32, 506)))
                .add(new GraphicPolyBezier("#666666", 2, new Point(37, 499),
                        new PolyBezierData(new Point(45, 459), new Point(77, 462), new Point(82, 466)),
                        new PolyBezierData(new Point(90, 453), new Point(65, 437), new Point(38, 463)),
                        new PolyBezierData(new Point(15, 496), new Point(26, 508), new Point(41, 502))))
                .add(new GraphicBezierCurve("#000000", 2, new Point(63, 475), new Point(63, 474), new Point(70, 474),
                        new Point(68, 482)))
                .add(new GraphicBezierCurve("#000000", 2, new Point(63, 475), new Point(62, 481), new Point(62, 481),
                        new Point(68, 482)))
                .add(new GraphicBezierCurve("#000000", 2, new Point(50, 487), new Point(52, 487), new Point(56, 488),
                        new Point(55, 492)))
                .add(new GraphicBezierCurve("#000000", 2, new Point(50, 487), new Point(48, 493), new Point(52, 494),
                        new Point(55, 492)))
                .add(new GraphicBezierCurve("#440e12", 2, new Point(54, 512), new Point(45, 529), new Point(41, 536),
                        new Point(50, 556)))
                .add(new GraphicBezierCurve("#440e12", 2, new Point(54, 512), new Point(53, 532), new Point(55, 534),
                        new Point(60, 542)))
                .add(new GraphicBezierCurve("#440e12", 2, new Point(60, 542), new Point(59, 551), new Point(60, 553),
                        new Point(50, 556)))
                .add(new GraphicBezierCurve("#440e12", 2, new Point(87, 487), new Point(82, 485), new Point(108, 490),
                        new Point(113, 501)))
                .add(new GraphicBezierCurve("#440e12", 2, new Point(87, 486), new Point(103, 483), new Point(106, 485),
                        new Point(125, 492)))
                .add(new GraphicBezierCurve("#440e12", 2, new Point(115, 501), new Point(114, 503), new Point(119, 503),
                        new Point(125, 501)))
                .add(new GraphicBezierCurve("#666666", 2, new Point(125, 501), new Point(122, 498), new Point(122, 494),
                        new Point(125, 492), new Point(138, 500), new Point(125, 501)))
                .add(new GraphicBezierCurve("#666666", 2, new Point(50, 556), new Point(46, 568), new Point(67, 560),
                        new Point(57, 553)))
                .add(new GraphicPolyBezier("#999999", 2, new Point(37, 499),
                        new PolyBezierData(new Point(50, 504), new Point(73, 494), new Point(82, 466)),
                        new PolyBezierData(new Point(86, 487), new Point(88, 495), new Point(86, 514)),
                        new PolyBezierData(new Point(66, 519), new Point(51, 514), new Point(37, 499))))
                .add(new GraphicBezierCurve("#440e12", 2, new Point(66, 550), new Point(83, 574), new Point(94, 560),
                        new Point(95, 557), new Point(100, 550), new Point(95, 535)))
                .add(new GraphicBezierCurve("#440e12", 2, new Point(95, 535), new Point(104, 549), new Point(114, 546),
                        new Point(114, 545)))
                .add(new GraphicBezierCurve("#440e12", 2, new Point(120, 510), new Point(123, 514), new Point(127, 522),
                        new Point(126, 532)))
                .add(new GraphicBezierCurve("#000000", 2, new Point(126, 532), new Point(117, 538), new Point(115, 542),
                        new Point(114, 545)))
                .add(new GraphicBezierCurve("#000000", 2, new Point(126, 532), new Point(146, 525), new Point(119, 563),
                        new Point(114, 545)))
                .add(new GraphicBezierCurve("#000000", 2, new Point(97, 555), new Point(109, 558), new Point(97, 567),
                        new Point(91, 572), new Point(76, 574), new Point(98, 555)))
                .add(new GraphicPolyBezier("#999999", 2, new Point(113, 501),
                        new PolyBezierData(new Point(92, 523), new Point(79, 531), new Point(61, 541)),
                        new PolyBezierData(new Point(62, 554), new Point(61, 545), new Point(65, 549),
                                new Point(116, 522), new Point(119, 509)),
                        new PolyBezierData(new Point(119, 504), new Point(118, 506), new Point(113, 501))))
                .add(new GraphicFloodFill("#ffffff", new Point(48, 469)))
                .add(new GraphicFloodFill("#ffffff", new Point(73, 502)))
                .add(new GraphicFloodFill("#ffffff", new Point(24, 509)))
                .add(new GraphicFloodFill("#ffffff", new Point(79, 534)))
                .add(new GraphicFloodFill("#ffffff", new Point(54, 558)))
                .add(new GraphicFloodFill("#ffffff", new Point(127, 497)))
                .add(new GraphicFloodFill("#82060e", new Point(104, 505)))
                .add(new GraphicCircle("#000000", 3, new Point(86, 519), 2))
                .add(new GraphicFloodFill("#82060e", new Point(113, 493)))
                .add(new GraphicFloodFill("#82060e", new Point(50, 539)))
                .add(new GraphicFloodFill("#82060e", new Point(82, 551)))
                .add(new GraphicFloodFill("#82060e", new Point(112, 531)))
                .add(new GraphicFloodFill("#82060e", new Point(41, 453)))
                .add(new GraphicFloodFill("#000000", new Point(65, 477)))
                .add(new GraphicFloodFill("#000000", new Point(52, 489)))
                .add(new GraphicFloodFill("#000000", new Point(125, 540)))
                .add(new GraphicFloodFill("#000000", new Point(92, 565)))
                .add(new GraphicFloodFill("#f4c89b", new Point(54, 479))));
        instructions.add(new GraphicLayer("dragon back arm")
                .setShown(true)
                .add(new GraphicBezierCurve("#000000", 2, new Point(283, 142), new Point(309, 155), new Point(315, 139),
                        new Point(333, 160)))
                .add(new GraphicBezierCurve("#000000", 2, new Point(228, 164), new Point(257, 166), new Point(258, 167),
                        new Point(298, 162)))
                .add(new GraphicBezierCurve("#000000", 2, new Point(283, 142), new Point(253, 140), new Point(249, 145),
                        new Point(228, 164)))
                .add(new GraphicBezierCurve("#000000", 2, new Point(333, 160), new Point(344, 174), new Point(303, 166),
                        new Point(298, 162)))
                .add(new GraphicFloodFill("#840b12", new Point(277, 154))));
        instructions.add(new GraphicLayer("dragon below")
                .setShown(true)
                .add(new GraphicBezierCurve("#300202", 2, new Point(458, 292), new Point(533, 354), new Point(495, 445),
                        new Point(435, 471)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(144, 421), new Point(203, 431), new Point(330, 334),
                        new Point(396, 345)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(396, 345), new Point(406, 350), new Point(428, 359),
                        new Point(392, 405)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(383, 421), new Point(402, 408), new Point(386, 382),
                        new Point(340, 417), new Point(309, 446), new Point(306, 458)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(361, 431), new Point(275, 494), new Point(223, 454),
                        new Point(253, 423), new Point(325, 380), new Point(356, 406)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(208, 485), new Point(204, 460), new Point(218, 394),
                        new Point(304, 393)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(438, 470), new Point(456, 483), new Point(400, 516),
                        new Point(367, 519), new Point(344, 511), new Point(363, 509)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(411, 489), new Point(371, 516), new Point(337, 519),
                        new Point(333, 517)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(325, 266), new Point(245, 285), new Point(282, 280),
                        new Point(240, 291)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(102, 324), new Point(150, 346), new Point(205, 304),
                        new Point(240, 291)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(330, 518), new Point(288, 550), new Point(274, 555),
                        new Point(231, 565)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(231, 565), new Point(275, 543), new Point(279, 534),
                        new Point(293, 519)))
                .add(new GraphicBezierCurve("#000000", 2, new Point(294, 543), new Point(283, 574), new Point(271, 572),
                        new Point(256, 584)))
                .add(new GraphicBezierCurve("#000000", 2, new Point(256, 584), new Point(273, 566), new Point(266, 570),
                        new Point(271, 562)))
                .add(new GraphicBezierCurve("#000000", 2, new Point(271, 562), new Point(266, 566), new Point(248, 572),
                        new Point(237, 596)))
                .add(new GraphicBezierCurve("#000000", 2, new Point(237, 596), new Point(240, 581), new Point(233, 584),
                        new Point(247, 569)))
                .add(new GraphicBezierCurve("#000000", 2, new Point(247, 569), new Point(223, 596), new Point(223, 591),
                        new Point(202, 592)))
                .add(new GraphicBezierCurve("#000000", 2, new Point(202, 592), new Point(225, 582), new Point(225, 580),
                        new Point(233, 571)))
                .add(new GraphicBezierCurve("#000000", 2, new Point(233, 571), new Point(204, 588), new Point(218, 571),
                        new Point(187, 588)))
                .add(new GraphicBezierCurve("#000000", 2, new Point(187, 588), new Point(195, 575), new Point(191, 572),
                        new Point(214, 565)))
                .add(new GraphicBezierCurve("#000000", 2, new Point(141, 580), new Point(161, 567), new Point(158, 585),
                        new Point(191, 576)))
                .add(new GraphicBezierCurve("#000000", 2, new Point(141, 580), new Point(166, 568), new Point(166, 550),
                        new Point(211, 552)))
                .add(new GraphicBezierCurve("#000000", 2, new Point(211, 552), new Point(186, 546), new Point(190, 546),
                        new Point(174, 543)))
                .add(new GraphicBezierCurve("#000000", 2, new Point(174, 543), new Point(215, 542), new Point(218, 550),
                        new Point(246, 533)))
                .add(new GraphicBezierCurve("#000000", 2, new Point(211, 537), new Point(229, 533), new Point(220, 530),
                        new Point(241, 535)))
                .add(new GraphicBezierCurve("#000000", 2, new Point(211, 537), new Point(234, 524), new Point(225, 522),
                        new Point(280, 531)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(333, 394), new Point(335, 408), new Point(336, 406),
                        new Point(328, 426)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(300, 395), new Point(303, 399), new Point(311, 405),
                        new Point(306, 429)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(273, 407), new Point(279, 411), new Point(284, 417),
                        new Point(282, 434)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(251, 425), new Point(253, 427), new Point(266, 428),
                        new Point(265, 444)))
                .add(new GraphicBezierCurve("#000000", 2, new Point(321, 432), new Point(294, 420), new Point(260, 444),
                        new Point(258, 460)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(353, 403), new Point(352, 403), new Point(361, 391),
                        new Point(366, 397)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(366, 397), new Point(358, 391), new Point(350, 390),
                        new Point(330, 392)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(330, 392), new Point(332, 389), new Point(334, 383),
                        new Point(344, 386)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(344, 386), new Point(321, 382), new Point(323, 383),
                        new Point(298, 393)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(291, 393), new Point(293, 388), new Point(294, 386),
                        new Point(308, 383)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(308, 383), new Point(289, 383), new Point(281, 383),
                        new Point(253, 403)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(220, 431), new Point(205, 442), new Point(207, 443),
                        new Point(204, 460)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(204, 460), new Point(195, 455), new Point(197, 449),
                        new Point(200, 447)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(200, 447), new Point(191, 464), new Point(197, 478),
                        new Point(200, 486)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(200, 486), new Point(194, 486), new Point(187, 462),
                        new Point(189, 466)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(189, 466), new Point(192, 504), new Point(207, 506),
                        new Point(237, 507)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(210, 505), new Point(213, 509), new Point(233, 522),
                        new Point(259, 510)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(222, 499), new Point(211, 497), new Point(208, 493),
                        new Point(204, 484)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(204, 484), new Point(226, 498), new Point(220, 490),
                        new Point(231, 499)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(228, 490), new Point(231, 504), new Point(245, 505),
                        new Point(252, 506)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(228, 490), new Point(249, 500), new Point(258, 499),
                        new Point(266, 501)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(256, 487), new Point(263, 494), new Point(266, 506),
                        new Point(282, 506)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(256, 487), new Point(283, 501), new Point(279, 500),
                        new Point(302, 503)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(283, 484), new Point(287, 498), new Point(293, 500),
                        new Point(310, 504)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(283, 484), new Point(304, 497), new Point(309, 497),
                        new Point(340, 493)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(307, 477), new Point(312, 487), new Point(318, 496),
                        new Point(340, 493)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(307, 476), new Point(318, 481), new Point(336, 489),
                        new Point(351, 479)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(326, 478), new Point(329, 476), new Point(353, 472),
                        new Point(357, 481)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(326, 478), new Point(346, 465), new Point(361, 466),
                        new Point(375, 469)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(350, 462), new Point(344, 462), new Point(365, 461),
                        new Point(368, 466)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(350, 462), new Point(374, 452), new Point(393, 460),
                        new Point(411, 445)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(385, 448), new Point(410, 440), new Point(394, 447),
                        new Point(418, 443)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(385, 448), new Point(432, 430), new Point(418, 434),
                        new Point(435, 428)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(408, 427), new Point(418, 429), new Point(412, 428),
                        new Point(431, 429)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(408, 427), new Point(407, 427), new Point(447, 417),
                        new Point(451, 401)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(427, 409), new Point(432, 417), new Point(461, 399),
                        new Point(461, 384)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(427, 409), new Point(438, 410), new Point(459, 392),
                        new Point(454, 363)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(456, 352), new Point(452, 374), new Point(449, 379),
                        new Point(445, 384)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(445, 384), new Point(451, 367), new Point(453, 365),
                        new Point(445, 346)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(445, 346), new Point(445, 359), new Point(443, 360),
                        new Point(438, 365)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(438, 365), new Point(437, 365), new Point(451, 354),
                        new Point(429, 328)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(428, 345), new Point(430, 342), new Point(435, 329),
                        new Point(422, 314)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(428, 345), new Point(428, 329), new Point(416, 321),
                        new Point(406, 320)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(408, 331), new Point(404, 319), new Point(404, 318),
                        new Point(396, 310)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(408, 331), new Point(390, 310), new Point(363, 312),
                        new Point(357, 312)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(357, 312), new Point(382, 319), new Point(374, 330),
                        new Point(382, 332)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(382, 332), new Point(347, 315), new Point(330, 323),
                        new Point(328, 324)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(296, 329), new Point(331, 321), new Point(338, 326),
                        new Point(355, 330)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(355, 330), new Point(328, 322), new Point(307, 338),
                        new Point(295, 348)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(326, 333), new Point(315, 354), new Point(292, 361),
                        new Point(274, 357)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(326, 333), new Point(299, 355), new Point(289, 349),
                        new Point(275, 344)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(275, 351), new Point(260, 380), new Point(230, 365),
                        new Point(219, 372)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(275, 351), new Point(262, 364), new Point(250, 358),
                        new Point(243, 359)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(249, 369), new Point(217, 377), new Point(219, 388),
                        new Point(197, 383)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(228, 386), new Point(207, 409), new Point(181, 386),
                        new Point(176, 398)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(228, 386), new Point(222, 391), new Point(208, 396),
                        new Point(197, 383)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(165, 399), new Point(184, 399), new Point(177, 399),
                        new Point(191, 404)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(190, 404), new Point(197, 405), new Point(171, 398),
                        new Point(152, 409)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(166, 409), new Point(144, 414), new Point(145, 411),
                        new Point(139, 411)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(166, 409), new Point(131, 428), new Point(124, 418),
                        new Point(118, 418)))
                .add(new GraphicBezierCurve("#712c22", 2, new Point(139, 422), new Point(118, 435), new Point(100, 414),
                        new Point(99, 415)))
                .add(new GraphicBezierCurve("#712c22", 2, new Point(113, 439), new Point(113, 428), new Point(102, 419),
                        new Point(97, 413)))
                .add(new GraphicBezierCurve("#712c22", 2, new Point(113, 439), new Point(108, 427), new Point(92, 415),
                        new Point(81, 414)))
                .add(new GraphicBezierCurve("#712c22", 2, new Point(81, 414), new Point(80, 417), new Point(101, 435),
                        new Point(102, 430)))
                .add(new GraphicBezierCurve("#712c22", 2, new Point(102, 430), new Point(96, 433), new Point(80, 429),
                        new Point(73, 418)))
                .add(new GraphicBezierCurve("#712c22", 2, new Point(73, 418), new Point(62, 409), new Point(66, 419),
                        new Point(56, 404)))
                .add(new GraphicBezierCurve("#712c22", 2, new Point(50, 389), new Point(49, 388), new Point(54, 408),
                        new Point(70, 426)))
                .add(new GraphicBezierCurve("#712c22", 2, new Point(70, 426), new Point(54, 420), new Point(29, 399),
                        new Point(37, 371)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(94, 372), new Point(66, 373), new Point(60, 358),
                        new Point(55, 351)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(94, 372), new Point(72, 382), new Point(62, 369),
                        new Point(59, 367)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(126, 367), new Point(104, 390), new Point(84, 373),
                        new Point(86, 375)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(126, 367), new Point(117, 388), new Point(107, 386),
                        new Point(105, 388)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(154, 368), new Point(130, 390), new Point(123, 390),
                        new Point(116, 384)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(154, 368), new Point(142, 384), new Point(152, 374),
                        new Point(143, 389)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(185, 360), new Point(137, 376), new Point(150, 388),
                        new Point(143, 389)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(185, 360), new Point(167, 373), new Point(172, 368),
                        new Point(164, 381)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(214, 345), new Point(196, 370), new Point(190, 366),
                        new Point(175, 369)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(215, 345), new Point(200, 377), new Point(199, 368),
                        new Point(191, 377)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(255, 330), new Point(235, 333), new Point(236, 334),
                        new Point(209, 360)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(255, 330), new Point(241, 337), new Point(250, 332),
                        new Point(235, 343)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(235, 343), new Point(269, 335), new Point(271, 321),
                        new Point(279, 309)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(279, 309), new Point(273, 333), new Point(275, 330),
                        new Point(262, 342)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(337, 286), new Point(305, 314), new Point(296, 304),
                        new Point(277, 320)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(337, 286), new Point(328, 299), new Point(336, 294),
                        new Point(314, 315)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(433, 297), new Point(376, 287), new Point(358, 287),
                        new Point(331, 300)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(459, 319), new Point(437, 299), new Point(451, 307),
                        new Point(405, 295)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(481, 335), new Point(431, 304), new Point(431, 308),
                        new Point(415, 310)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(481, 335), new Point(467, 331), new Point(475, 335),
                        new Point(455, 327)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(484, 355), new Point(479, 344), new Point(475, 336),
                        new Point(466, 333)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(483, 389), new Point(493, 379), new Point(486, 353),
                        new Point(472, 345)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(476, 414), new Point(472, 408), new Point(492, 370),
                        new Point(477, 358)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(476, 414), new Point(473, 398), new Point(471, 410),
                        new Point(475, 391)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(451, 438), new Point(456, 437), new Point(466, 425),
                        new Point(473, 408)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(451, 438), new Point(462, 423), new Point(463, 419),
                        new Point(466, 406)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(429, 464), new Point(456, 446), new Point(448, 451),
                        new Point(457, 436)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(386, 483), new Point(414, 481), new Point(432, 460),
                        new Point(441, 447)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(386, 483), new Point(403, 475), new Point(410, 469),
                        new Point(417, 459)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(354, 519), new Point(353, 519), new Point(354, 499),
                        new Point(392, 484)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(354, 519), new Point(352, 515), new Point(353, 494),
                        new Point(372, 487)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(355, 502), new Point(339, 518), new Point(323, 511),
                        new Point(328, 509)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(328, 509), new Point(332, 511), new Point(347, 507),
                        new Point(347, 501)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(291, 510), new Point(303, 527), new Point(334, 521),
                        new Point(331, 514)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(291, 510), new Point(311, 517), new Point(312, 512),
                        new Point(318, 511)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(298, 518), new Point(264, 535), new Point(246, 511),
                        new Point(249, 515)))
                .add(new GraphicLine("#840b12", 2, new Point(54, 348), new Point(101, 323)))
                .add(new GraphicLine("#fc816f", 2, new Point(37, 370), new Point(57, 345)))
                .add(new GraphicFloodFill("#840b12", new Point(349, 514)))
                .add(new GraphicFloodFill("#840b12", new Point(347, 436)))
                .add(new GraphicBezierCurve("#000000", 2, new Point(325, 266), new Point(407, 262), new Point(424, 265),
                        new Point(458, 292)))
                .add(new GraphicFloodFill("#840b12", new Point(312, 339)))
                .add(new GraphicFloodFill("#840b12", new Point(424, 480)))
                .add(new GraphicFloodFill("#fc816f", new Point(422, 450)))
                .add(new GraphicFloodFill("#fc816f", new Point(282, 390)))
                .add(new GraphicFloodFill("#fc816f", new Point(325, 386)))
                .add(new GraphicFloodFill("#fc816f", new Point(350, 395)))
                .add(new GraphicFloodFill("#fc816f", new Point(261, 517)))
                .add(new GraphicFloodFill("#fc816f", new Point(319, 518)))
                .add(new GraphicFloodFill("#fc816f", new Point(237, 552)))
                .add(new GraphicFloodFill("#fc816f", new Point(347, 510)))
                .add(new GraphicFloodFill("#840b12", new Point(295, 532)))
                .add(new GraphicFloodFill("#840b12", new Point(351, 517)))
                .add(new GraphicFloodFill("#f7830a", new Point(343, 406)))
                .add(new GraphicFloodFill("#f7830a", new Point(322, 413)))
                .add(new GraphicFloodFill("#f7830a", new Point(292, 412)))
                .add(new GraphicFloodFill("#f7830a", new Point(271, 423)))
                .add(new GraphicFloodFill("#f7830a", new Point(255, 440))));
        instructions.add(new GraphicLayer("new year sign")
                .setShown(true)
                .add(new GraphicLine("#ffed00", 2, new Point(237, 174), new Point(197, 271)))
                .add(new GraphicLine("#a27000", 2, new Point(252, 177), new Point(213, 273)))
                .add(new GraphicLine("#c38700", 2, new Point(582, 284), new Point(511, 359)))
                .add(new GraphicLine("#6d4b00", 2, new Point(593, 295), new Point(521, 368)))
                .add(new GraphicBezierCurve("#fffdd8", 2, new Point(233, 179), new Point(222, 164), new Point(246, 164),
                        new Point(252, 154)))
                .add(new GraphicBezierCurve("#7e6807", 2, new Point(252, 154), new Point(253, 170), new Point(271, 172),
                        new Point(250, 184)))
                .add(new GraphicBezierCurve("#a27000", 2, new Point(237, 174), new Point(244, 170), new Point(249, 172),
                        new Point(252, 177)))
                .add(new GraphicBezierCurve("#fffdd8", 2, new Point(204, 270), new Point(162, 280), new Point(205, 299),
                        new Point(199, 302)))
                .add(new GraphicBezierCurve("#7e6807", 2, new Point(223, 283), new Point(221, 297), new Point(213, 294),
                        new Point(199, 302)))
                .add(new GraphicBezierCurve("#7e6807", 2, new Point(223, 283), new Point(221, 278), new Point(219, 273),
                        new Point(204, 270)))
                .add(new GraphicFloodFill("#ffed00", new Point(197, 276)))
                .add(new GraphicBezierCurve("#7e6807", 2, new Point(212, 292), new Point(209, 285), new Point(196, 280),
                        new Point(189, 287)))
                .add(new GraphicBezierCurve("#fffdd8", 2, new Point(203, 287), new Point(202, 288), new Point(196, 282),
                        new Point(191, 289)))
                .add(new GraphicBezierCurve("#7e6807", 2, new Point(521, 368), new Point(521, 369), new Point(516, 351),
                        new Point(498, 359), new Point(501, 381), new Point(498, 386)))
                .add(new GraphicBezierCurve("#4d3f03", 2, new Point(521, 368), new Point(527, 387), new Point(498, 386),
                        new Point(499, 385)))
                .add(new GraphicFloodFill("#ffed00", new Point(501, 378)))
                .add(new GraphicBezierCurve("#7e6807", 2, new Point(494, 371), new Point(505, 370), new Point(510, 379),
                        new Point(510, 382)))
                .add(new GraphicBezierCurve("#a27000", 2, new Point(582, 284), new Point(588, 281), new Point(597, 284),
                        new Point(593, 294)))
                .add(new GraphicBezierCurve("#7e6807", 2, new Point(582, 284), new Point(573, 273), new Point(588, 266),
                        new Point(600, 267)))
                .add(new GraphicBezierCurve("#4d3f03", 2, new Point(593, 294), new Point(612, 296), new Point(601, 275),
                        new Point(601, 265)))
                .add(new GraphicBezierCurve("#f7f6fb", 2, new Point(260, 178), new Point(330, 145), new Point(378, 164),
                        new Point(417, 190)))
                .add(new GraphicBezierCurve("#f7f6fb", 2, new Point(417, 190), new Point(460, 210), new Point(467, 191),
                        new Point(512, 206), new Point(551, 263), new Point(579, 283)))
                .add(new GraphicBezierCurve("#f7f6fb", 2, new Point(339, 267), new Point(338, 240), new Point(367, 196),
                        new Point(368, 165)))
                .add(new GraphicBezierCurve("#f7f6fb", 2, new Point(420, 291), new Point(433, 278), new Point(437, 221),
                        new Point(458, 200)))
                .add(new GraphicBezierCurve("#f7f6fb", 2, new Point(482, 301), new Point(484, 284), new Point(523, 233),
                        new Point(531, 219)))
                .add(new GraphicBezierCurve("#728ab0", 2, new Point(220, 274), new Point(333, 252), new Point(352, 275),
                        new Point(359, 281)))
                .add(new GraphicBezierCurve("#728ab0", 2, new Point(359, 281), new Point(402, 305), new Point(433, 285),
                        new Point(458, 291), new Point(490, 307), new Point(508, 357)))
                .add(new GraphicFloodFill("#e9b000", new Point(228, 213)))
                .add(new GraphicFloodFill("#ffed00", new Point(236, 166)))
                .add(new GraphicFloodFill("#e9b000", new Point(533, 345)))
                .add(new GraphicFloodFill("#ffed00", new Point(592, 276)))
                .add(new GraphicFloodFill("#ffed00", new Point(513, 371)))
                .add(new GraphicFloodFill("#f7f6fb", new Point(249, 216)))
                .add(new GraphicFloodFill("#e2e5ec", new Point(396, 236)))
                .add(new GraphicFloodFill("#f7f6fb", new Point(484, 235)))
                .add(new GraphicFloodFill("#e2e5ec", new Point(536, 287))));
        instructions.add(new GraphicLayer("new year text")
                .setShown(true)
                .add(new GraphicPolyline("#045968", 2, true, new Point(251, 249), new Point(263, 207),
                        new Point(288, 210), new Point(291, 199), new Point(265, 196), new Point(268, 185),
                        new Point(298, 189), new Point(302, 182), new Point(311, 183), new Point(295, 250),
                        new Point(281, 250), new Point(289, 221), new Point(270, 218), new Point(264, 236),
                        new Point(273, 236), new Point(270, 250)))
                .add(new GraphicPolyline("#1e6d6b", 2, true, new Point(307, 252), new Point(309, 239),
                        new Point(320, 240), new Point(329, 203), new Point(314, 202), new Point(316, 189),
                        new Point(340, 193), new Point(325, 256)))
                .add(new GraphicPolyline("#1e5b59", 2, true, new Point(324, 173), new Point(322, 182),
                        new Point(346, 188), new Point(350, 180), new Point(334, 179), new Point(335, 173)))
                .add(new GraphicPolyline("#136657", 2, true, new Point(331, 255), new Point(341, 216),
                        new Point(365, 221), new Point(367, 210), new Point(342, 204), new Point(345, 194),
                        new Point(371, 201), new Point(373, 186), new Point(381, 188), new Point(363, 266),
                        new Point(354, 262), new Point(360, 234), new Point(346, 231), new Point(344, 243),
                        new Point(350, 244), new Point(346, 260)))
                .add(new GraphicPolyline("#0a6b4b", 2, true, new Point(402, 245), new Point(380, 277),
                        new Point(368, 272), new Point(386, 212), new Point(419, 221), new Point(401, 284),
                        new Point(392, 280), new Point(409, 226), new Point(393, 225), new Point(384, 262),
                        new Point(392, 242)))
                .add(new GraphicPolyline("#086b4b", 2, true, new Point(391, 202), new Point(419, 215),
                        new Point(427, 203), new Point(420, 199), new Point(416, 205), new Point(393, 194)))
                .add(new GraphicPolyline("#106840", 2, true, new Point(425, 225), new Point(418, 244),
                        new Point(427, 244), new Point(417, 282), new Point(447, 280), new Point(458, 210),
                        new Point(450, 210), new Point(441, 269), new Point(427, 270), new Point(438, 226)))
                .add(new GraphicPolyline("#0f6641", 2, true, new Point(429, 219), new Point(446, 222),
                        new Point(450, 205), new Point(442, 205), new Point(442, 213), new Point(429, 211)))
                .add(new GraphicPolyline("#0f682a", 2, true, new Point(465, 270), new Point(469, 272),
                        new Point(463, 286), new Point(452, 279), new Point(477, 219), new Point(471, 216),
                        new Point(464, 233), new Point(457, 228), new Point(466, 207), new Point(489, 215)))
                .add(new GraphicPolyline("#3b6b10", 2, true, new Point(481, 252), new Point(487, 256),
                        new Point(470, 287), new Point(477, 289), new Point(498, 272), new Point(488, 298),
                        new Point(494, 299), new Point(506, 271), new Point(515, 268), new Point(519, 258),
                        new Point(510, 249), new Point(502, 261), new Point(487, 270), new Point(502, 244),
                        new Point(490, 239)))
                .add(new GraphicPolyline("#3b6b10", 2, true, new Point(512, 258), new Point(508, 265)))
                .add(new GraphicPolyline("#566812", 2, true, new Point(519, 274), new Point(521, 281),
                        new Point(514, 291), new Point(510, 286), new Point(498, 305), new Point(505, 317),
                        new Point(512, 308), new Point(512, 328), new Point(547, 293), new Point(542, 284),
                        new Point(520, 306), new Point(520, 294), new Point(535, 274), new Point(527, 265)))
                .add(new GraphicPolyline("#566812", 2, true, new Point(511, 296), new Point(505, 304)))
                .add(new GraphicPolyline("#586b12", 2, true, new Point(547, 282), new Point(551, 286),
                        new Point(563, 274), new Point(558, 269)))
                .add(new GraphicFloodFill("#0cb5d4", new Point(258, 245)))
                .add(new GraphicFloodFill("#43e4df", new Point(310, 247)))
                .add(new GraphicFloodFill("#4fe4e0", new Point(328, 175)))
                .add(new GraphicFloodFill("#29d0b4", new Point(337, 248)))
                .add(new GraphicFloodFill("#1ad097", new Point(398, 246)))
                .add(new GraphicFloodFill("#14d498", new Point(398, 200)))
                .add(new GraphicFloodFill("#25d283", new Point(425, 236)))
                .add(new GraphicFloodFill("#21d387", new Point(431, 214)))
                .add(new GraphicFloodFill("#20d457", new Point(463, 266)))
                .add(new GraphicFloodFill("#74d120", new Point(485, 249)))
                .add(new GraphicFloodFill("#aed226", new Point(524, 273)))
                .add(new GraphicFloodFill("#b4da25", new Point(553, 279))));
        instructions.add(new GraphicLayer("dragon above")
                .setShown(true)
                .add(new GraphicBezierCurve("#300202", 2, new Point(334, 105), new Point(295, 102), new Point(276, 104),
                        new Point(205, 144)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(363, 127), new Point(305, 127), new Point(241, 151),
                        new Point(211, 175)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(197, 140), new Point(215, 151), new Point(205, 147),
                        new Point(216, 150), new Point(209, 170), new Point(196, 210)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(170, 218), new Point(179, 209), new Point(201, 212),
                        new Point(252, 212)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(252, 212), new Point(265, 218), new Point(276, 230),
                        new Point(272, 231)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(272, 231), new Point(275, 240), new Point(273, 242),
                        new Point(269, 253)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(269, 253), new Point(274, 245), new Point(268, 238),
                        new Point(262, 235)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(262, 235), new Point(266, 257), new Point(266, 240),
                        new Point(264, 252)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(264, 252), new Point(244, 265), new Point(261, 255),
                        new Point(244, 264)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(244, 264), new Point(264, 249), new Point(254, 243),
                        new Point(249, 238)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(249, 238), new Point(245, 254), new Point(248, 260),
                        new Point(223, 263)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(223, 263), new Point(250, 252), new Point(233, 243),
                        new Point(229, 234)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(229, 234), new Point(196, 229), new Point(197, 241),
                        new Point(177, 244), new Point(168, 247), new Point(150, 244)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(150, 244), new Point(148, 245), new Point(158, 224),
                        new Point(153, 220)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(153, 220), new Point(158, 211), new Point(186, 188),
                        new Point(157, 177)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(135, 329), new Point(77, 279), new Point(147, 188),
                        new Point(163, 182)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(135, 329), new Point(95, 294), new Point(153, 234),
                        new Point(151, 233)))
                .add(new GraphicBezierCurve("#712c22", 2, new Point(22, 399), new Point(37, 386), new Point(33, 385),
                        new Point(36, 376)))
                .add(new GraphicBezierCurve("#712c22", 2, new Point(22, 399), new Point(27, 386), new Point(32, 376),
                        new Point(20, 359)))
                .add(new GraphicBezierCurve("#712c22", 2, new Point(18, 328), new Point(14, 340), new Point(25, 360),
                        new Point(37, 361)))
                .add(new GraphicBezierCurve("#712c22", 2, new Point(37, 361), new Point(1, 361), new Point(5, 327),
                        new Point(7, 321)))
                .add(new GraphicLine("#712c22", 2, new Point(5, 334), new Point(0, 327)))
                .add(new GraphicBezierCurve("#712c22", 2, new Point(13, 277), new Point(12, 278), new Point(3, 289),
                        new Point(0, 301)))
                .add(new GraphicBezierCurve("#712c22", 2, new Point(13, 245), new Point(10, 247), new Point(2, 252),
                        new Point(0, 266)))
                .add(new GraphicBezierCurve("#712c22", 2, new Point(29, 195), new Point(30, 201), new Point(6, 211),
                        new Point(0, 228)))
                .add(new GraphicBezierCurve("#712c22", 2, new Point(20, 205), new Point(-3, 211), new Point(15, 207),
                        new Point(0, 208)))
                .add(new GraphicBezierCurve("#712c22", 2, new Point(0, 208), new Point(21, 198), new Point(27, 190),
                        new Point(29, 164)))
                .add(new GraphicBezierCurve("#712c22", 2, new Point(20, 174), new Point(28, 161), new Point(26, 159),
                        new Point(47, 152)))
                .add(new GraphicBezierCurve("#712c22", 2, new Point(20, 174), new Point(31, 132), new Point(62, 123),
                        new Point(79, 126)))
                .add(new GraphicBezierCurve("#712c22", 2, new Point(38, 126), new Point(56, 122), new Point(48, 121),
                        new Point(66, 125)))
                .add(new GraphicBezierCurve("#712c22", 2, new Point(38, 126), new Point(95, 110), new Point(82, 103),
                        new Point(92, 88)))
                .add(new GraphicBezierCurve("#712c22", 2, new Point(112, 78), new Point(101, 79), new Point(78, 91),
                        new Point(79, 106)))
                .add(new GraphicBezierCurve("#712c22", 2, new Point(79, 106), new Point(76, 81), new Point(103, 58),
                        new Point(136, 60)))
                .add(new GraphicBezierCurve("#712c22", 2, new Point(136, 60), new Point(93, 63), new Point(89, 59),
                        new Point(82, 47)))
                .add(new GraphicBezierCurve("#712c22", 2, new Point(82, 47), new Point(104, 74), new Point(139, 32),
                        new Point(190, 52)))
                .add(new GraphicBezierCurve("#712c22", 2, new Point(171, 48), new Point(157, 43), new Point(153, 34),
                        new Point(150, 25)))
                .add(new GraphicBezierCurve("#712c22", 2, new Point(150, 25), new Point(162, 34), new Point(181, 48),
                        new Point(198, 32)))
                .add(new GraphicBezierCurve("#712c22", 2, new Point(226, 34), new Point(212, 26), new Point(199, 28),
                        new Point(183, 35)))
                .add(new GraphicBezierCurve("#712c22", 2, new Point(183, 35), new Point(193, 24), new Point(196, 17),
                        new Point(225, 21), new Point(237, 30), new Point(266, 19)))
                .add(new GraphicBezierCurve("#712c22", 2, new Point(292, 13), new Point(278, 7), new Point(259, -3),
                        new Point(235, 23)))
                .add(new GraphicBezierCurve("#712c22", 2, new Point(337, 21), new Point(292, -10), new Point(280, 5),
                        new Point(275, 5)))
                .add(new GraphicBezierCurve("#712c22", 2, new Point(321, 0), new Point(338, 9), new Point(332, 6),
                        new Point(341, 13)))
                .add(new GraphicBezierCurve("#712c22", 2, new Point(375, 21), new Point(364, 5), new Point(342, 11),
                        new Point(334, -3)))
                .add(new GraphicLine("#712c22", 2, new Point(338, 0), new Point(341, 13)))
                .add(new GraphicBezierCurve("#712c22", 2, new Point(406, 34), new Point(388, 19), new Point(368, 27),
                        new Point(351, 12)))
                .add(new GraphicBezierCurve("#712c22", 2, new Point(351, 12), new Point(358, 27), new Point(356, 22),
                        new Point(361, 26)))
                .add(new GraphicBezierCurve("#712c22", 2, new Point(378, 3), new Point(402, 29), new Point(422, 17),
                        new Point(427, 48)))
                .add(new GraphicBezierCurve("#712c22", 2, new Point(378, 3), new Point(377, 9), new Point(377, 12),
                        new Point(388, 25)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(443, 66), new Point(448, 55), new Point(409, 40),
                        new Point(385, 37)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(385, 37), new Point(382, 37), new Point(402, 44),
                        new Point(403, 53)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(367, 68), new Point(373, 51), new Point(417, 50),
                        new Point(423, 60)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(367, 68), new Point(373, 65), new Point(399, 62),
                        new Point(413, 68)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(364, 87), new Point(373, 64), new Point(409, 72),
                        new Point(404, 65)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(431, 52), new Point(428, 48), new Point(434, 42),
                        new Point(422, 21)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(422, 21), new Point(439, 40), new Point(437, 40),
                        new Point(439, 45), new Point(454, 58), new Point(452, 70)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(446, 24), new Point(442, 33), new Point(441, 32),
                        new Point(442, 50)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(446, 24), new Point(447, 64), new Point(459, 41),
                        new Point(452, 70)))
                .add(new GraphicBezierCurve("#000000", 2, new Point(396, 47), new Point(342, 40), new Point(375, 22),
                        new Point(326, 10)))
                .add(new GraphicBezierCurve("#000000", 2, new Point(326, 10), new Point(351, 22), new Point(329, 34),
                        new Point(375, 58)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(392, 87), new Point(385, 95), new Point(343, 92),
                        new Point(346, 89)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(346, 89), new Point(364, 105), new Point(375, 105),
                        new Point(394, 100)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(325, 92), new Point(329, 96), new Point(348, 102),
                        new Point(366, 101)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(325, 92), new Point(345, 120), new Point(359, 115),
                        new Point(382, 105)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(350, 114), new Point(365, 126), new Point(379, 126),
                        new Point(393, 125)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(351, 133), new Point(361, 129), new Point(359, 126),
                        new Point(371, 130)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(351, 133), new Point(367, 139), new Point(377, 146),
                        new Point(391, 139)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(377, 146), new Point(384, 147), new Point(387, 145),
                        new Point(391, 139)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(377, 146), new Point(398, 151), new Point(402, 141),
                        new Point(423, 137)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(404, 148), new Point(423, 146), new Point(416, 139),
                        new Point(425, 136)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(404, 148), new Point(432, 160), new Point(433, 146),
                        new Point(444, 141)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(434, 149), new Point(446, 150), new Point(442, 143),
                        new Point(461, 141)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(511, 137), new Point(516, 167), new Point(463, 127),
                        new Point(401, 138)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(516, 127), new Point(520, 149), new Point(454, 128),
                        new Point(471, 130)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(471, 130), new Point(427, 120), new Point(447, 125),
                        new Point(428, 119)))
                .add(new GraphicBezierCurve("#000000", 2, new Point(516, 128), new Point(481, 123), new Point(508, 103),
                        new Point(516, 127)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(500, 116), new Point(474, 106), new Point(480, 93),
                        new Point(480, 93), new Point(467, 82), new Point(452, 69)))
                .add(new GraphicBezierCurve("#000000", 2, new Point(471, 113), new Point(339, 162), new Point(391, 102),
                        new Point(309, 123)))
                .add(new GraphicBezierCurve("#000000", 2, new Point(483, 118), new Point(421, 142), new Point(428, 153),
                        new Point(393, 150), new Point(346, 167), new Point(351, 174)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(426, 82), new Point(450, 106), new Point(437, 94),
                        new Point(469, 103)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(426, 82), new Point(460, 88), new Point(458, 88),
                        new Point(469, 103)))
                .add(new GraphicBezierCurve("#000000", 2, new Point(453, 88), new Point(449, 94), new Point(449, 91),
                        new Point(454, 100)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(452, 69), new Point(425, 66), new Point(442, 61),
                        new Point(403, 79)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(481, 105), new Point(484, 106), new Point(481, 112),
                        new Point(471, 109)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(490, 112), new Point(485, 116), new Point(491, 114),
                        new Point(480, 115)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(400, 138), new Point(411, 133), new Point(423, 128),
                        new Point(399, 123)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(399, 123), new Point(422, 118), new Point(400, 114),
                        new Point(392, 103)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(392, 103), new Point(432, 114), new Point(391, 94),
                        new Point(397, 93)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(401, 80), new Point(409, 85), new Point(405, 83),
                        new Point(414, 87)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(414, 87), new Point(409, 88), new Point(411, 89),
                        new Point(400, 88)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(400, 88), new Point(403, 92), new Point(403, 92),
                        new Point(411, 97)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(411, 97), new Point(411, 96), new Point(405, 99),
                        new Point(397, 93)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(346, 82), new Point(344, 86), new Point(317, 84),
                        new Point(308, 79)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(308, 79), new Point(322, 89), new Point(316, 92),
                        new Point(327, 96)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(327, 96), new Point(291, 88), new Point(275, 97),
                        new Point(266, 105)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(266, 105), new Point(278, 84), new Point(309, 82),
                        new Point(315, 88)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(249, 76), new Point(255, 82), new Point(257, 84),
                        new Point(282, 90)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(267, 86), new Point(244, 90), new Point(242, 88),
                        new Point(231, 102)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(231, 102), new Point(230, 96), new Point(239, 82),
                        new Point(248, 83)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(248, 83), new Point(217, 86), new Point(245, 81),
                        new Point(190, 110)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(190, 110), new Point(207, 90), new Point(211, 93),
                        new Point(218, 78)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(210, 89), new Point(177, 95), new Point(170, 117),
                        new Point(172, 119)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(172, 119), new Point(166, 97), new Point(190, 83),
                        new Point(186, 86)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(137, 136), new Point(146, 114), new Point(158, 103),
                        new Point(175, 94)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(137, 136), new Point(139, 131), new Point(131, 117),
                        new Point(148, 101)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(114, 146), new Point(130, 137), new Point(130, 128),
                        new Point(137, 117)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(114, 146), new Point(126, 127), new Point(122, 127),
                        new Point(123, 115)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(122, 130), new Point(106, 138), new Point(104, 146),
                        new Point(103, 161)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(103, 161), new Point(95, 152), new Point(99, 137),
                        new Point(103, 135)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(94, 185), new Point(89, 180), new Point(88, 149),
                        new Point(99, 146)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(94, 185), new Point(75, 177), new Point(86, 145),
                        new Point(87, 149)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(78, 209), new Point(72, 208), new Point(74, 176),
                        new Point(83, 165)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(78, 209), new Point(66, 211), new Point(65, 175),
                        new Point(69, 173)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(58, 237), new Point(49, 214), new Point(61, 208),
                        new Point(68, 192)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(58, 237), new Point(40, 227), new Point(52, 204),
                        new Point(52, 204)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(49, 252), new Point(46, 245), new Point(44, 228),
                        new Point(48, 222)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(49, 252), new Point(33, 245), new Point(40, 214),
                        new Point(38, 220)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(54, 295), new Point(22, 272), new Point(39, 251),
                        new Point(41, 245)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(54, 295), new Point(40, 296), new Point(37, 291),
                        new Point(35, 290)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(35, 290), new Point(42, 309), new Point(37, 308),
                        new Point(63, 328)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(63, 328), new Point(49, 330), new Point(35, 322),
                        new Point(32, 308)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(74, 350), new Point(44, 347), new Point(41, 328),
                        new Point(41, 324)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(74, 350), new Point(49, 354), new Point(37, 344),
                        new Point(37, 343)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(323, 131), new Point(322, 113), new Point(314, 110),
                        new Point(306, 106)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(295, 136), new Point(299, 141), new Point(287, 111),
                        new Point(276, 111)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(255, 148), new Point(256, 145), new Point(252, 132),
                        new Point(239, 125)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(165, 201), new Point(166, 200), new Point(158, 191),
                        new Point(153, 192)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(146, 238), new Point(149, 232), new Point(137, 219),
                        new Point(132, 220)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(131, 262), new Point(133, 271), new Point(131, 251),
                        new Point(117, 252)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(222, 165), new Point(221, 173), new Point(230, 159),
                        new Point(218, 158)))
                .add(new GraphicLine("#fc816f", 2, new Point(38, 374), new Point(54, 350)))
                .add(new GraphicLine("#840b12", 2, new Point(133, 331), new Point(57, 344)))
                .add(new GraphicFloodFill("#840b12", new Point(126, 181)))
                .add(new GraphicFloodFill("#fc816f", new Point(55, 141)))
                .add(new GraphicFloodFill("#fc816f", new Point(310, 89)))
                .add(new GraphicFloodFill("#000000", new Point(351, 34)))
                .add(new GraphicFloodFill("#fc816f", new Point(376, 30)))
                .add(new GraphicFloodFill("#fc816f", new Point(435, 46)))
                .add(new GraphicFloodFill("#fc816f", new Point(445, 46)))
                .add(new GraphicFloodFill("#fc816f", new Point(347, 12)))
                .add(new GraphicFloodFill("#fc816f", new Point(317, 1)))
                .add(new GraphicFloodFill("#fc816f", new Point(385, 117)))
                .add(new GraphicFloodFill("#fc816f", new Point(349, 105)))
                .add(new GraphicFloodFill("#fc816f", new Point(372, 133)))
                .add(new GraphicFloodFill("#fc816f", new Point(426, 140)))
                .add(new GraphicFloodFill("#fc816f", new Point(446, 141)))
                .add(new GraphicFloodFill("#840b12", new Point(431, 106)))
                .add(new GraphicFloodFill("#840b12", new Point(437, 130)))
                .add(new GraphicFloodFill("#840b12", new Point(474, 137)))
                .add(new GraphicFloodFill("#ffffff", new Point(443, 91)))
                .add(new GraphicFloodFill("#000000", new Point(459, 93)))
                .add(new GraphicFloodFill("#000000", new Point(505, 121)))
                .add(new GraphicFloodFill("#f7830a", new Point(331, 120)))
                .add(new GraphicFloodFill("#f7830a", new Point(326, 110)))
                .add(new GraphicFloodFill("#f7830a", new Point(297, 118)))
                .add(new GraphicFloodFill("#f7830a", new Point(266, 128)))
                .add(new GraphicFloodFill("#f7830a", new Point(236, 143)))
                .add(new GraphicFloodFill("#f7830a", new Point(218, 163)))
                .add(new GraphicFloodFill("#f7830a", new Point(163, 189)))
                .add(new GraphicFloodFill("#f7830a", new Point(150, 208)))
                .add(new GraphicFloodFill("#f7830a", new Point(134, 238)))
                .add(new GraphicFloodFill("#f7830a", new Point(119, 268)))
                .add(new GraphicBezierCurve("#300202", 2, new Point(102, 324), new Point(150, 346), new Point(205, 304),
                        new Point(240, 291))));

        GraphicsPanel panel = new GraphicsPanel(instructions);

        JFrame frame = new JFrame();
        frame.add(panel);
        frame.setTitle("MMXXIV");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

}

class GraphicLayer {
    public boolean shown = true;
    public String name;
    public List<GraphicObject> objects;

    GraphicLayer(String name) {
        this.name = name;
        this.objects = new ArrayList<>();
    }

    GraphicLayer(String name, List<GraphicObject> objects) {
        this.name = name;
        this.objects = objects;
    }

    BufferedImage draw() {
        BufferedImage buffer = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
        for (GraphicObject object : objects) {
            object.draw(buffer);
        }
        return buffer;
    }

    GraphicLayer setShown(boolean shown) {
        this.shown = shown;
        return this;
    }

    GraphicLayer add(GraphicObject object) {
        objects.add(object);
        return this;
    }

    GraphicLayer remove(GraphicObject object) {
        objects.remove(object);
        return this;
    }

}

abstract class GraphicObject {
    abstract public void draw(BufferedImage buffer);
}

class ColorHexer {

    public static Color decode(String hex) {
        return decodeOptional(hex).orElse(Color.black);
    }

    // formats:
    // #RGB
    // #RGBA
    // #RRGGBB
    // #RRGGBBAA
    public static Optional<Color> decodeOptional(String hex) {
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }
        try {
            Long.parseLong(hex, 16);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }

        if (hex.length() == 3) {
            return Optional.of(Color.decode("#" +
                    hex.substring(0, 1).repeat(2) +
                    hex.substring(1, 2).repeat(2) +
                    hex.substring(2, 3).repeat(2)));
        } else if (hex.length() == 4) {
            return Optional.of(new Color(
                    Integer.parseInt(hex.substring(0, 1), 16) * 17,
                    Integer.parseInt(hex.substring(1, 2), 16) * 17,
                    Integer.parseInt(hex.substring(2, 3), 16) * 17,
                    Integer.parseInt(hex.substring(3, 4), 16) * 17));
        } else if (hex.length() == 6) {
            return Optional.of(Color.decode("#" + hex));
        } else if (hex.length() == 8) {
            return Optional.of(new Color(
                    Integer.parseInt(hex.substring(0, 2), 16),
                    Integer.parseInt(hex.substring(2, 4), 16),
                    Integer.parseInt(hex.substring(4, 6), 16),
                    Integer.parseInt(hex.substring(6, 8), 16)));
        } else {
            return Optional.empty();
        }
    }

    public static String encode(Color c) {
        if (c.getAlpha() == 255) {
            return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
        } else {
            return String.format("#%02x%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
        }
    }
}

abstract class GraphicPlotter extends GraphicObject {
    public Color color;

    protected GraphicPlotter(String hexColor) {
        this.color = ColorHexer.decode(hexColor);
    }

    protected void plot(Graphics g, int x, int y, int size) {
        g.fillRect(x - size / 2, y - size / 2, size, size);
    }
}

abstract class GraphicLinePlotter extends GraphicPlotter {
    public int thickness;

    protected GraphicLinePlotter(String hexColor, int thickness) {
        super(hexColor);
        this.thickness = thickness;
    }

    protected void plotLine(Graphics g, Point p1, Point p2) {
        int dx = Math.abs(p2.x - p1.x);
        int dy = Math.abs(p2.y - p1.y);

        int sx = (p1.x < p2.x) ? 1 : -1;
        int sy = (p1.y < p2.y) ? 1 : -1;
        boolean isSwap = false;

        if (dy > dx) {
            int temp = dy;
            dy = dx;
            dx = temp;
            isSwap = true;
        }
        int D = 2 * dy - dx;

        int x = p1.x;
        int y = p1.y;
        for (int i = 1; i <= dx; i++) {
            plot(g, x, y, thickness);
            if (D >= 0) {
                if (isSwap) {
                    x += sx;
                } else {
                    y += sy;
                }
                D -= 2 * dx;
            }

            if (isSwap) {
                y += sy;
            } else {
                x += sx;
            }
            D += 2 * dy;
        }
    }
}

class GraphicLine extends GraphicLinePlotter {
    public Point p1, p2;

    GraphicLine(String hexColor, int thickness, Point p1, Point p2) {
        super(hexColor, thickness);
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public void draw(BufferedImage buffer) {
        Graphics g = buffer.createGraphics();
        g.setColor(color);

        plotLine(g, p1, p2);
    }

}

class GraphicPolygon extends GraphicPlotter {
    public List<Point> points;

    GraphicPolygon(String hexColor, Point... points) {
        this(hexColor, new ArrayList<>(Arrays.asList(points)));
    }

    GraphicPolygon(String hexColor, List<Point> points) {
        super(hexColor);
        this.points = points;
    }

    @Override
    public void draw(BufferedImage buffer) {
        Graphics2D g = buffer.createGraphics();
        g.setColor(color);

        Polygon poly = new Polygon();
        for (Point point : points) {
            poly.addPoint(point.x, point.y);
        }
        g.fillPolygon(poly);
    }

}

class GraphicPolyline extends GraphicLinePlotter {
    public boolean closed;
    public List<Point> points;

    GraphicPolyline(String hexColor, int thickness, boolean closed, Point... points) {
        this(hexColor, thickness, closed, new ArrayList<>(Arrays.asList(points)));
    }

    GraphicPolyline(String hexColor, int thickness, boolean closed, List<Point> points) {
        super(hexColor, thickness);
        this.closed = closed;
        this.points = points;
    }

    @Override
    public void draw(BufferedImage buffer) {
        Graphics g = buffer.createGraphics();
        g.setColor(color);

        for (int i = 1; i < points.size(); i++) {
            plotLine(g, points.get(i - 1), points.get(i));
        }
        if (closed) {
            plotLine(g, points.get(points.size() - 1), points.get(0));
        }
    }

}

abstract class GraphicBezierPlotter extends GraphicPlotter {
    private static final int BEZIER_ITERATIONS = 2000;
    public int thickness;

    protected GraphicBezierPlotter(String hexColor, int thickness) {
        super(hexColor);
        this.thickness = thickness;
    }

    protected void plotBezier(Graphics g, Point pA, Point pB, Point pC, Point pD) {
        plotBezier(g, pA, pB, pC, pD, BEZIER_ITERATIONS);
    }

    protected void plotBezier(Graphics g, Point pA, Point pB, Point pC, Point pD, int iterations) {
        for (int i = 0; i < iterations; i++) {
            double t = i / (double) iterations;

            double x = Math.pow(1 - t, 3) * pA.x +
                    3 * t * Math.pow(1 - t, 2) * pB.x +
                    3 * Math.pow(t, 2) * (1 - t) * pC.x
                    + Math.pow(t, 3) * pD.x;

            double y = Math.pow(1 - t, 3) * pA.y +
                    3 * t * Math.pow(1 - t, 2) * pB.y +
                    3 * Math.pow(t, 2) * (1 - t) * pC.y
                    + Math.pow(t, 3) * pD.y;

            plot(g, (int) Math.round(x), (int) Math.round(y), thickness);
        }
    }
}

class GraphicBezierCurve extends GraphicBezierPlotter {
    public Point p1, p2;
    public List<Point> continuedPoints;

    GraphicBezierCurve(String hexColor, int thickness, Point p1, Point p2, Point... continuedPoints) {
        this(hexColor, thickness, p1, p2, new ArrayList<>(Arrays.asList(continuedPoints)));
    }

    GraphicBezierCurve(String hexColor, int thickness, Point p1, Point p2, List<Point> continuedPoints) {
        super(hexColor, thickness);
        this.p1 = p1;
        this.p2 = p2;
        this.continuedPoints = continuedPoints;
    }

    @Override
    public void draw(BufferedImage buffer) {
        Graphics g = buffer.createGraphics();
        g.setColor(color);

        plotBezier(g, p1, p2, continuedPoints.get(0), continuedPoints.get(1));
        for (int i = 3; i < continuedPoints.size(); i += 2) {
            Point pA = continuedPoints.get(i - 2);
            Point pBtemp = continuedPoints.get(i - 3);
            Point pB = new Point(pA.x + (pA.x - pBtemp.x), pA.y + (pA.y - pBtemp.y));
            Point pC = continuedPoints.get(i - 1);
            Point pD = continuedPoints.get(i);

            plotBezier(g, pA, pB, pC, pD);
        }
    }

}

class PolyBezierData {
    public Point p2;
    public List<Point> morePoints;

    PolyBezierData(Point p2, Point... morePoints) {
        this(p2, new ArrayList<>(Arrays.asList(morePoints)));
    }

    PolyBezierData(Point p2, List<Point> morePoints) {
        this.p2 = p2;
        this.morePoints = morePoints;
    }

}

class GraphicPolyBezier extends GraphicBezierPlotter {
    public Point p1;
    public List<PolyBezierData> data;

    GraphicPolyBezier(String hexColor, int thickness, Point p1, PolyBezierData... data) {
        this(hexColor, thickness, p1, new ArrayList<>(Arrays.asList(data)));
    }

    GraphicPolyBezier(String hexColor, int thickness, Point p1, List<PolyBezierData> data) {
        super(hexColor, thickness);
        this.p1 = p1;
        this.data = data;
    }

    @Override
    public void draw(BufferedImage buffer) {
        Graphics g = buffer.createGraphics();
        g.setColor(color);

        Point pNextA = p1;

        for (PolyBezierData d : data) {
            plotBezier(g, pNextA, d.p2, d.morePoints.get(0), d.morePoints.get(1));
            for (int i = 3; i < d.morePoints.size(); i += 2) {
                Point pA = d.morePoints.get(i - 2);
                Point pBtemp = d.morePoints.get(i - 3);
                Point pB = new Point(pA.x + (pA.x - pBtemp.x), pA.y + (pA.y - pBtemp.y));
                Point pC = d.morePoints.get(i - 1);
                Point pD = d.morePoints.get(i);

                plotBezier(g, pA, pB, pC, pD);
            }
            pNextA = d.morePoints.get(d.morePoints.size() - 1);
        }
    }

}

// https://stackoverflow.com/q/1734745/3623350
class GraphicCircle extends GraphicBezierPlotter {
    private static final double BEZIER_CIRCLE_CONSTANT = 0.552284749831;
    public Point center;
    public int radius;

    GraphicCircle(String hexColor, int thickness, Point center, int radius) {
        super(hexColor, thickness);
        this.center = center;
        this.radius = radius;
    }

    private Point roundPoint(double x, double y) {
        return new Point((int) Math.round(x), (int) Math.round(y));
    }

    @Override
    public void draw(BufferedImage buffer) {
        Graphics g = buffer.createGraphics();
        g.setColor(color);
        double offset = radius * BEZIER_CIRCLE_CONSTANT;
        double perimeter = radius * 2 * Math.PI;
        int iters = (int) Math.round(perimeter);

        plotBezier(g, roundPoint(center.x, center.y - radius),
                roundPoint(center.x + offset, center.y - radius),
                roundPoint(center.x + radius, center.y - offset),
                roundPoint(center.x + radius, center.y), iters);

        plotBezier(g, roundPoint(center.x, center.y + radius),
                roundPoint(center.x + offset, center.y + radius),
                roundPoint(center.x + radius, center.y + offset),
                roundPoint(center.x + radius, center.y), iters);

        plotBezier(g, roundPoint(center.x, center.y + radius),
                roundPoint(center.x - offset, center.y + radius),
                roundPoint(center.x - radius, center.y + offset),
                roundPoint(center.x - radius, center.y), iters);

        plotBezier(g, roundPoint(center.x, center.y - radius),
                roundPoint(center.x - offset, center.y - radius),
                roundPoint(center.x - radius, center.y - offset),
                roundPoint(center.x - radius, center.y), iters);
    }

}

class GraphicFloodFill extends GraphicPlotter {
    public Point point;

    GraphicFloodFill(String hexColor, Point point) {
        super(hexColor);
        this.point = point;
    }

    @Override
    public void draw(BufferedImage buffer) {
        if (!(0 <= point.x &&
                point.x < buffer.getWidth() &&
                0 <= point.y &&
                point.y < buffer.getHeight())) {
            return;
        }
        if (buffer.getRGB(point.x, point.y) == color.getRGB()) {
            return;
        }

        Queue<Point> q = new ArrayDeque<>();
        q.add(point);
        Color target_color = new Color(buffer.getRGB(point.x, point.y), true);

        for (Point p = q.poll(); p != null; p = q.poll()) {
            if (!(0 <= p.x &&
                    p.x < buffer.getWidth() &&
                    0 <= p.y &&
                    p.y < buffer.getHeight())) {
                continue;
            }

            if (buffer.getRGB(p.x, p.y) == target_color.getRGB()) {
                buffer.setRGB(p.x, p.y, color.getRGB());
                q.add(new Point(p.x + 1, p.y));
                q.add(new Point(p.x - 1, p.y));
                q.add(new Point(p.x, p.y + 1));
                q.add(new Point(p.x, p.y - 1));
            }
        }
    }

}

class GraphicsPanel extends JPanel {
    List<GraphicLayer> instructions;

    GraphicsPanel(List<GraphicLayer> instructions) {
        super();
        this.setPreferredSize(new Dimension(600, 600));
        this.instructions = instructions;
    }

    @Override
    protected void paintComponent(Graphics gOuter) {
        super.paintComponent(gOuter);

        for (GraphicLayer layer : instructions) {
            if (layer.shown) {
                gOuter.drawImage(layer.draw(), 0, 0, null);
            }
        }
    }

}
