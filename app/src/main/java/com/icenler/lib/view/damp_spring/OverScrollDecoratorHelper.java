package com.icenler.lib.view.damp_spring;

import android.support.annotation.IntDef;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.icenler.lib.view.damp_spring.adapters.AbsListViewOverScrollDecorAdapter;
import com.icenler.lib.view.damp_spring.adapters.HorizontalScrollViewOverScrollDecorAdapter;
import com.icenler.lib.view.damp_spring.adapters.RecyclerViewOverScrollDecorAdapter;
import com.icenler.lib.view.damp_spring.adapters.ScrollViewOverScrollDecorAdapter;
import com.icenler.lib.view.damp_spring.adapters.StaticOverScrollDecorAdapter;
import com.icenler.lib.view.damp_spring.adapters.ViewPagerOverScrollDecorAdapter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by iCenler - 2016/7/5.
 * Description：阻尼弹簧动效
 */
public class OverScrollDecoratorHelper {

    public static final int ORIENTATION_VERTICAL = 0;
    public static final int ORIENTATION_HORIZONTAL = 1;

    @IntDef({ORIENTATION_VERTICAL, ORIENTATION_HORIZONTAL})
    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.SOURCE)
    public @interface ORIENTATION {
    }

    /**
     * Set up the over-scroll effect over a specified {@link RecyclerView} view.
     * <br/>Only recycler-views using <b>native</b> Android layout managers (i.e. {@link LinearLayoutManager},
     * {@link GridLayoutManager} and {@link StaggeredGridLayoutManager}) are currently supported
     * by this convenience method.
     *
     * @param recyclerView The view.
     * @param orientation  Either {@link #ORIENTATION_HORIZONTAL} or {@link #ORIENTATION_VERTICAL}.
     * @return The over-scroll effect 'decorator', enabling further effect configuration.
     */
    public static IOverScrollDecor setUpOverScroll(RecyclerView recyclerView, @ORIENTATION int orientation) {
        switch (orientation) {
            case ORIENTATION_HORIZONTAL:
                return new HorizontalOverScrollBounceEffectDecorator(new RecyclerViewOverScrollDecorAdapter(recyclerView));
            case ORIENTATION_VERTICAL:
                return new VerticalOverScrollBounceEffectDecorator(new RecyclerViewOverScrollDecorAdapter(recyclerView));
            default:
                throw new IllegalArgumentException("orientation");
        }
    }

    public static IOverScrollDecor setUpOverScroll(ListView listView) {
        return new VerticalOverScrollBounceEffectDecorator(new AbsListViewOverScrollDecorAdapter(listView));
    }

    public static IOverScrollDecor setUpOverScroll(GridView gridView) {
        return new VerticalOverScrollBounceEffectDecorator(new AbsListViewOverScrollDecorAdapter(gridView));
    }

    public static IOverScrollDecor setUpOverScroll(ViewPager viewPager) {
        return new HorizontalOverScrollBounceEffectDecorator(new ViewPagerOverScrollDecorAdapter(viewPager));
    }

    public static IOverScrollDecor setUpOverScroll(ScrollView scrollView) {
        return new VerticalOverScrollBounceEffectDecorator(new ScrollViewOverScrollDecorAdapter(scrollView));
    }

    public static IOverScrollDecor setUpOverScroll(HorizontalScrollView scrollView) {
        return new HorizontalOverScrollBounceEffectDecorator(new HorizontalScrollViewOverScrollDecorAdapter(scrollView));
    }

    /**
     * Set up the over-scroll over a generic view, assumed to always be over-scroll ready (e.g.
     * a plain text field, image view).
     *
     * @param view        The view.
     * @param orientation One of {@link #ORIENTATION_HORIZONTAL} or {@link #ORIENTATION_VERTICAL}.
     * @return The over-scroll effect 'decorator', enabling further effect configuration.
     */
    public static IOverScrollDecor setUpStaticOverScroll(View view, @ORIENTATION int orientation) {
        switch (orientation) {
            case ORIENTATION_HORIZONTAL:
                return new HorizontalOverScrollBounceEffectDecorator(new StaticOverScrollDecorAdapter(view));

            case ORIENTATION_VERTICAL:
                return new VerticalOverScrollBounceEffectDecorator(new StaticOverScrollDecorAdapter(view));

            default:
                throw new IllegalArgumentException("orientation");
        }
    }

}
