package guhong.play.mybatisplusenhancer.base.criteria;


import cn.hutool.core.util.ReflectUtil;
import guhong.play.mybatisplusenhancer.annotation.criteria.*;
import guhong.play.mybatisplusenhancer.handler.criteria.CriteriaHandler;
import guhong.play.mybatisplusenhancer.handler.criteria.impl.*;
import lombok.Data;
import lombok.NonNull;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 条件匹配容器
 *
 * @author : 李双凯
 * @date : 2022/4/22 16:03
 **/
@Data
public class CriteriaContainer {

    private static final Map<Class<? extends Annotation>, Class<? extends CriteriaHandler>> PARAM_ANNOTATIONS_MAP = new LinkedHashMap<>();

    static {
        PARAM_ANNOTATIONS_MAP.put(Eq.class, EqHandler.class);
        PARAM_ANNOTATIONS_MAP.put(Ge.class, GeHandler.class);
        PARAM_ANNOTATIONS_MAP.put(Gt.class, GtHandler.class);
        PARAM_ANNOTATIONS_MAP.put(In.class, InHandler.class);
        PARAM_ANNOTATIONS_MAP.put(Le.class, LeHandler.class);
        PARAM_ANNOTATIONS_MAP.put(LikeLeft.class, LikeLeftHandler.class);
        PARAM_ANNOTATIONS_MAP.put(LikeRight.class, LikeRightHandler.class);
        PARAM_ANNOTATIONS_MAP.put(Like.class, LikeHandler.class);
        PARAM_ANNOTATIONS_MAP.put(Lt.class, LtHandler.class);
        PARAM_ANNOTATIONS_MAP.put(Ne.class, NeHandler.class);
        PARAM_ANNOTATIONS_MAP.put(NotIn.class, NotInHandler.class);
        PARAM_ANNOTATIONS_MAP.put(Set.class, SetHandler.class);
    }

    public static List<CriteriaHandler> findCriteriaHandlerList(@NonNull Annotation[] paramAnnotations) {
        List<CriteriaHandler> handlerList = new ArrayList<>();
        for (Annotation annotation : paramAnnotations) {
            Class<? extends CriteriaHandler> criteriaHandlerClass = PARAM_ANNOTATIONS_MAP.get(annotation.annotationType());
            if (criteriaHandlerClass != null) {
                CriteriaHandler criteriaHandler = ReflectUtil.newInstance(criteriaHandlerClass);
                criteriaHandler.setParamAnnotation(annotation);
                handlerList.add(criteriaHandler);
            }
        }
        return handlerList;
    }


}
