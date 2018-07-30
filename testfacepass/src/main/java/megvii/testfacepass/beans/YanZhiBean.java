package megvii.testfacepass.beans;

import android.graphics.Bitmap;

import java.util.List;

import megvii.facepass.types.FacePassFace;

/**
 * Created by Administrator on 2018/7/27.
 */

public class YanZhiBean {

    /**
     * image_id : YDqHg/1yPPfq5Ea/TRy9Lg==
     * request_id : 1532681808,dc53f002-1da1-438d-a05c-19eda5233edc
     * time_used : 242
     * faces : [{"attributes":{"emotion":{"sadness":4.135,"neutral":94.091,"disgust":0.002,"anger":0.002,"surprise":1.765,"fear":0.003,"happiness":0.002},"glass":{"value":"None"},"eyestatus":{"left_eye_status":{"normal_glass_eye_open":0.01,"no_glass_eye_close":0,"occlusion":0,"no_glass_eye_open":99.99,"normal_glass_eye_close":0,"dark_glasses":0},"right_eye_status":{"normal_glass_eye_open":0.008,"no_glass_eye_close":0,"occlusion":0,"no_glass_eye_open":99.992,"normal_glass_eye_close":0,"dark_glasses":0}},"beauty":{"female_score":66.408,"male_score":66.856}},"face_rectangle":{"width":370,"top":5,"left":3,"height":370},"face_token":"f31e084223ead678cf5373999d44b1df"}]
     */
    private Bitmap bitmap;
    private String xiehouyu;
    private FacePassFace facePassFace;
    private String request_id;
    private long time_used;
    private List<FacesBean> faces;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getXiehouyu() {
        return xiehouyu;
    }

    public void setXiehouyu(String xiehouyu) {
        this.xiehouyu = xiehouyu;
    }

    public FacePassFace getFacePassFace() {
        return facePassFace;
    }

    public void setFacePassFace(FacePassFace facePassFace) {
        this.facePassFace = facePassFace;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public long getTime_used() {
        return time_used;
    }

    public void setTime_used(long time_used) {
        this.time_used = time_used;
    }

    public List<FacesBean> getFaces() {
        return faces;
    }

    public void setFaces(List<FacesBean> faces) {
        this.faces = faces;
    }

    public static class FacesBean {
        /**
         * attributes : {"emotion":{"sadness":4.135,"neutral":94.091,"disgust":0.002,"anger":0.002,"surprise":1.765,"fear":0.003,"happiness":0.002},"glass":{"value":"None"},"eyestatus":{"left_eye_status":{"normal_glass_eye_open":0.01,"no_glass_eye_close":0,"occlusion":0,"no_glass_eye_open":99.99,"normal_glass_eye_close":0,"dark_glasses":0},"right_eye_status":{"normal_glass_eye_open":0.008,"no_glass_eye_close":0,"occlusion":0,"no_glass_eye_open":99.992,"normal_glass_eye_close":0,"dark_glasses":0}},"beauty":{"female_score":66.408,"male_score":66.856}}
         * face_rectangle : {"width":370,"top":5,"left":3,"height":370}
         * face_token : f31e084223ead678cf5373999d44b1df
         */

        private AttributesBean attributes;
        private FaceRectangleBean face_rectangle;
        private String face_token;

        public AttributesBean getAttributes() {
            return attributes;
        }

        public void setAttributes(AttributesBean attributes) {
            this.attributes = attributes;
        }

        public FaceRectangleBean getFace_rectangle() {
            return face_rectangle;
        }

        public void setFace_rectangle(FaceRectangleBean face_rectangle) {
            this.face_rectangle = face_rectangle;
        }

        public String getFace_token() {
            return face_token;
        }

        public void setFace_token(String face_token) {
            this.face_token = face_token;
        }

        public static class AttributesBean {
            /**
             * emotion : {"sadness":4.135,"neutral":94.091,"disgust":0.002,"anger":0.002,"surprise":1.765,"fear":0.003,"happiness":0.002}
             * glass : {"value":"None"}
             * eyestatus : {"left_eye_status":{"normal_glass_eye_open":0.01,"no_glass_eye_close":0,"occlusion":0,"no_glass_eye_open":99.99,"normal_glass_eye_close":0,"dark_glasses":0},"right_eye_status":{"normal_glass_eye_open":0.008,"no_glass_eye_close":0,"occlusion":0,"no_glass_eye_open":99.992,"normal_glass_eye_close":0,"dark_glasses":0}}
             * beauty : {"female_score":66.408,"male_score":66.856}
             */

            private EmotionBean emotion;
            private GlassBean glass;
            private EyestatusBean eyestatus;
            private BeautyBean beauty;

            public EmotionBean getEmotion() {
                return emotion;
            }

            public void setEmotion(EmotionBean emotion) {
                this.emotion = emotion;
            }

            public GlassBean getGlass() {
                return glass;
            }

            public void setGlass(GlassBean glass) {
                this.glass = glass;
            }

            public EyestatusBean getEyestatus() {
                return eyestatus;
            }

            public void setEyestatus(EyestatusBean eyestatus) {
                this.eyestatus = eyestatus;
            }

            public BeautyBean getBeauty() {
                return beauty;
            }

            public void setBeauty(BeautyBean beauty) {
                this.beauty = beauty;
            }

            public static class EmotionBean {
                /**
                 * sadness : 4.135
                 * neutral : 94.091
                 * disgust : 0.002
                 * anger : 0.002
                 * surprise : 1.765
                 * fear : 0.003
                 * happiness : 0.002
                 */

                private double sadness;
                private double neutral;
                private double disgust;
                private double anger;
                private double surprise;
                private double fear;
                private double happiness;

                public double getSadness() {
                    return sadness;
                }

                public void setSadness(double sadness) {
                    this.sadness = sadness;
                }

                public double getNeutral() {
                    return neutral;
                }

                public void setNeutral(double neutral) {
                    this.neutral = neutral;
                }

                public double getDisgust() {
                    return disgust;
                }

                public void setDisgust(double disgust) {
                    this.disgust = disgust;
                }

                public double getAnger() {
                    return anger;
                }

                public void setAnger(double anger) {
                    this.anger = anger;
                }

                public double getSurprise() {
                    return surprise;
                }

                public void setSurprise(double surprise) {
                    this.surprise = surprise;
                }

                public double getFear() {
                    return fear;
                }

                public void setFear(double fear) {
                    this.fear = fear;
                }

                public double getHappiness() {
                    return happiness;
                }

                public void setHappiness(double happiness) {
                    this.happiness = happiness;
                }
            }

            public static class GlassBean {
                /**
                 * value : None
                 */

                private String value;

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }
            }

            public static class EyestatusBean {
                /**
                 * left_eye_status : {"normal_glass_eye_open":0.01,"no_glass_eye_close":0,"occlusion":0,"no_glass_eye_open":99.99,"normal_glass_eye_close":0,"dark_glasses":0}
                 * right_eye_status : {"normal_glass_eye_open":0.008,"no_glass_eye_close":0,"occlusion":0,"no_glass_eye_open":99.992,"normal_glass_eye_close":0,"dark_glasses":0}
                 */

                private LeftEyeStatusBean left_eye_status;
                private RightEyeStatusBean right_eye_status;

                public LeftEyeStatusBean getLeft_eye_status() {
                    return left_eye_status;
                }

                public void setLeft_eye_status(LeftEyeStatusBean left_eye_status) {
                    this.left_eye_status = left_eye_status;
                }

                public RightEyeStatusBean getRight_eye_status() {
                    return right_eye_status;
                }

                public void setRight_eye_status(RightEyeStatusBean right_eye_status) {
                    this.right_eye_status = right_eye_status;
                }

                public static class LeftEyeStatusBean {
                    /**
                     * normal_glass_eye_open : 0.01
                     * no_glass_eye_close : 0.0
                     * occlusion : 0.0
                     * no_glass_eye_open : 99.99
                     * normal_glass_eye_close : 0.0
                     * dark_glasses : 0.0
                     */

                    private double normal_glass_eye_open;
                    private double no_glass_eye_close;
                    private double occlusion;
                    private double no_glass_eye_open;
                    private double normal_glass_eye_close;
                    private double dark_glasses;

                    public double getNormal_glass_eye_open() {
                        return normal_glass_eye_open;
                    }

                    public void setNormal_glass_eye_open(double normal_glass_eye_open) {
                        this.normal_glass_eye_open = normal_glass_eye_open;
                    }

                    public double getNo_glass_eye_close() {
                        return no_glass_eye_close;
                    }

                    public void setNo_glass_eye_close(double no_glass_eye_close) {
                        this.no_glass_eye_close = no_glass_eye_close;
                    }

                    public double getOcclusion() {
                        return occlusion;
                    }

                    public void setOcclusion(double occlusion) {
                        this.occlusion = occlusion;
                    }

                    public double getNo_glass_eye_open() {
                        return no_glass_eye_open;
                    }

                    public void setNo_glass_eye_open(double no_glass_eye_open) {
                        this.no_glass_eye_open = no_glass_eye_open;
                    }

                    public double getNormal_glass_eye_close() {
                        return normal_glass_eye_close;
                    }

                    public void setNormal_glass_eye_close(double normal_glass_eye_close) {
                        this.normal_glass_eye_close = normal_glass_eye_close;
                    }

                    public double getDark_glasses() {
                        return dark_glasses;
                    }

                    public void setDark_glasses(double dark_glasses) {
                        this.dark_glasses = dark_glasses;
                    }
                }

                public static class RightEyeStatusBean {
                    /**
                     * normal_glass_eye_open : 0.008
                     * no_glass_eye_close : 0.0
                     * occlusion : 0.0
                     * no_glass_eye_open : 99.992
                     * normal_glass_eye_close : 0.0
                     * dark_glasses : 0.0
                     */

                    private double normal_glass_eye_open;
                    private double no_glass_eye_close;
                    private double occlusion;
                    private double no_glass_eye_open;
                    private double normal_glass_eye_close;
                    private double dark_glasses;

                    public double getNormal_glass_eye_open() {
                        return normal_glass_eye_open;
                    }

                    public void setNormal_glass_eye_open(double normal_glass_eye_open) {
                        this.normal_glass_eye_open = normal_glass_eye_open;
                    }

                    public double getNo_glass_eye_close() {
                        return no_glass_eye_close;
                    }

                    public void setNo_glass_eye_close(double no_glass_eye_close) {
                        this.no_glass_eye_close = no_glass_eye_close;
                    }

                    public double getOcclusion() {
                        return occlusion;
                    }

                    public void setOcclusion(double occlusion) {
                        this.occlusion = occlusion;
                    }

                    public double getNo_glass_eye_open() {
                        return no_glass_eye_open;
                    }

                    public void setNo_glass_eye_open(double no_glass_eye_open) {
                        this.no_glass_eye_open = no_glass_eye_open;
                    }

                    public double getNormal_glass_eye_close() {
                        return normal_glass_eye_close;
                    }

                    public void setNormal_glass_eye_close(double normal_glass_eye_close) {
                        this.normal_glass_eye_close = normal_glass_eye_close;
                    }

                    public double getDark_glasses() {
                        return dark_glasses;
                    }

                    public void setDark_glasses(double dark_glasses) {
                        this.dark_glasses = dark_glasses;
                    }
                }
            }

            public static class BeautyBean {
                /**
                 * female_score : 66.408
                 * male_score : 66.856
                 */

                private double female_score;
                private double male_score;

                public double getFemale_score() {
                    return female_score;
                }

                public void setFemale_score(double female_score) {
                    this.female_score = female_score;
                }

                public double getMale_score() {
                    return male_score;
                }

                public void setMale_score(double male_score) {
                    this.male_score = male_score;
                }
            }
        }

        public static class FaceRectangleBean {
            /**
             * width : 370
             * top : 5
             * left : 3
             * height : 370
             */

            private int width;
            private int top;
            private int left;
            private int height;

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getTop() {
                return top;
            }

            public void setTop(int top) {
                this.top = top;
            }

            public int getLeft() {
                return left;
            }

            public void setLeft(int left) {
                this.left = left;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }
        }
    }
}
