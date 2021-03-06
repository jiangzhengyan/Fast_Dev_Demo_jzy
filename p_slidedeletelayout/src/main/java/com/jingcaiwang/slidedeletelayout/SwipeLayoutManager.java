package com.jingcaiwang.slidedeletelayout;

/**
 * Created by jiang_yan on 2017/11/13.
 */

public class SwipeLayoutManager {

    private SwipeLayoutManager() {
    }

    private static SwipeLayoutManager mInstance = new SwipeLayoutManager();

    public static SwipeLayoutManager getInstance() {
        return mInstance;
    }

    private SwipeLayout openSwipeLayout;

    /**
     * 记录已经打开的SwipeLayout
     *
     * @param openSwipeLayout
     */
    public void setOpenSwipeLayout(SwipeLayout openSwipeLayout) {

        if (this.openSwipeLayout != null) {
            this.openSwipeLayout.close();
        }
        this.openSwipeLayout = openSwipeLayout;

    }


    /**
     * 判断当前是否能够滑动
     *
     * @return
     */
    public boolean isCanSwipe(SwipeLayout currentLayout) {
        if (openSwipeLayout == null) {
            //说明没有打开的，那么则可以滑动
            return true;
        } else {
            //如果当前有打开的，那么判断打开的和当前触摸的是否是同一个，如果是同一个，那么可以滑动，反之不可以
            return openSwipeLayout == currentLayout;
        }
    }

    public void clearSwipeLayout() {
        openSwipeLayout = null;

    }

    /**
     * 关闭已经打开的SwipeLayout
     */
    public void closeSwipeLayout() {
        if (openSwipeLayout != null) {
            openSwipeLayout.close();
        }
    }

}
