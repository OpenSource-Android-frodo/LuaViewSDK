package com.taobao.luaview.global;

import android.util.Log;

import com.taobao.luaview.fun.binder.constants.AlignBinder;
import com.taobao.luaview.fun.binder.constants.EllipsizeBinder;
import com.taobao.luaview.fun.binder.constants.FontStyleBinder;
import com.taobao.luaview.fun.binder.constants.FontWeightBinder;
import com.taobao.luaview.fun.binder.constants.GravityBinder;
import com.taobao.luaview.fun.binder.constants.InterpolatorBinder;
import com.taobao.luaview.fun.binder.constants.ScaleTypeBinder;
import com.taobao.luaview.fun.binder.constants.TextAlignBinder;
import com.taobao.luaview.fun.binder.indicator.UICircleViewPagerIndicatorBinder;
import com.taobao.luaview.fun.binder.indicator.UICustomViewPagerIndicatorBinder;
import com.taobao.luaview.fun.binder.kit.ActionBarBinder;
import com.taobao.luaview.fun.binder.kit.AudioBinder;
import com.taobao.luaview.fun.binder.kit.DataBinder;
import com.taobao.luaview.fun.binder.kit.DownloaderBinder;
import com.taobao.luaview.fun.binder.kit.JsonBinder;
import com.taobao.luaview.fun.binder.kit.SystemBinder;
import com.taobao.luaview.fun.binder.kit.TimerBinder;
import com.taobao.luaview.fun.binder.kit.UnicodeBinder;
import com.taobao.luaview.fun.binder.kit.VibratorBinder;
import com.taobao.luaview.fun.binder.net.HttpBinder;
import com.taobao.luaview.fun.binder.ui.SpannableStringBinder;
import com.taobao.luaview.fun.binder.ui.UIAlertBinder;
import com.taobao.luaview.fun.binder.ui.UIAnimatorBinder;
import com.taobao.luaview.fun.binder.ui.UIButtonBinder;
import com.taobao.luaview.fun.binder.ui.UIEditTextBinder;
import com.taobao.luaview.fun.binder.ui.UIHorizontalScrollViewBinder;
import com.taobao.luaview.fun.binder.ui.UIImageViewBinder;
import com.taobao.luaview.fun.binder.ui.UIListViewBinder;
import com.taobao.luaview.fun.binder.ui.UILoadingDialogBinder;
import com.taobao.luaview.fun.binder.ui.UILoadingViewBinder;
import com.taobao.luaview.fun.binder.ui.UIRecyclerViewBinder;
import com.taobao.luaview.fun.binder.ui.UIRefreshListViewBinder;
import com.taobao.luaview.fun.binder.ui.UIRefreshRecyclerViewBinder;
import com.taobao.luaview.fun.binder.ui.UITextViewBinder;
import com.taobao.luaview.fun.binder.ui.UIToastBinder;
import com.taobao.luaview.fun.binder.ui.UIViewGroupBinder;
import com.taobao.luaview.fun.binder.ui.UIViewPagerBinder;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.LibFunction;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LuaView Lib管理等
 *
 * @author song
 * @date 15/8/14
 */
public class LuaViewManager {

    private static Map<String, List<LuaValue>> binders = new HashMap<>();

    public static void initBinders() {
        long start = System.nanoTime();
        Log.i("Binders", "init Binders --- start");
        //ui
        List<LuaValue> uiBinders = new ArrayList<>();
        uiBinders.add(new UITextViewBinder());
        uiBinders.add(new UIEditTextBinder());
        uiBinders.add(new UIButtonBinder());
        uiBinders.add(new UIImageViewBinder());
        uiBinders.add(new UIViewGroupBinder());
        uiBinders.add(new UIListViewBinder());
        uiBinders.add(new UIRecyclerViewBinder());
        uiBinders.add(new UIRefreshListViewBinder());
        uiBinders.add(new UIRefreshRecyclerViewBinder());
        uiBinders.add(new UIViewPagerBinder());
        uiBinders.add(new UICustomViewPagerIndicatorBinder());
        uiBinders.add(new UICircleViewPagerIndicatorBinder());
        uiBinders.add(new UIHorizontalScrollViewBinder());
        uiBinders.add(new UIAlertBinder());
        uiBinders.add(new UILoadingViewBinder());
        uiBinders.add(new UILoadingDialogBinder());
        uiBinders.add(new UIToastBinder());
        uiBinders.add(new SpannableStringBinder());

        //animation
        List<LuaValue> animationBinders = new ArrayList<>();
        animationBinders.add(new UIAnimatorBinder());

        //net
        List<LuaValue> netBinders = new ArrayList<>();
        netBinders.add(new UIAnimatorBinder());

        //kit
        List<LuaValue> kitBinders = new ArrayList<>();
        kitBinders.add(new TimerBinder());
        kitBinders.add(new SystemBinder());
        kitBinders.add(new ActionBarBinder());
        kitBinders.add(new DownloaderBinder());
        kitBinders.add(new UnicodeBinder());
        kitBinders.add(new DataBinder());
        kitBinders.add(new JsonBinder());
        kitBinders.add(new AudioBinder());
        kitBinders.add(new VibratorBinder());

        //常量
        List<LuaValue> constantsBinders = new ArrayList<>();
        constantsBinders.add(new AlignBinder());
        constantsBinders.add(new TextAlignBinder());
        constantsBinders.add(new FontWeightBinder());
        constantsBinders.add(new FontStyleBinder());
        constantsBinders.add(new ScaleTypeBinder());
        constantsBinders.add(new GravityBinder());
        constantsBinders.add(new EllipsizeBinder());
        constantsBinders.add(new InterpolatorBinder());

        binders.put("ui", uiBinders);
        binders.put("animation", animationBinders);
        binders.put("net", netBinders);
        binders.put("kit", kitBinders);
        binders.put("constants", constantsBinders);
        Log.i("Binders", "init Binders --- end, used: " + (System.nanoTime() - start) + "ns");
    }

    /**
     * load Android API lib
     * TODO 能否做到按需加载，而不是首次进来加载全部binder
     *
     * @param globals
     */
    public static void loadLuaViewLibs(final Globals globals) {
        long start = System.nanoTime();
        Log.i("Binders", "loadLuaViewLibs  @"+globals.hashCode() + " ----  start");
        for (String key : binders.keySet()) {
            for (LuaValue luaValue : binders.get(key)) {
                globals.load(luaValue);
            }
        }
        Log.i("Binders", "loadLuaViewLibs  @"+globals.hashCode() + " ----  end, used: " + (System.nanoTime() - start) + "ns");
    }

    /**
     * bind lua function
     *
     * @param factory
     * @param methods
     */
    public static LuaTable bind(Class<? extends LibFunction> factory, Method[] methods) {
        if (methods != null) {
            return bind(factory, Arrays.asList(methods));
        }
        return new LuaTable();
    }

    /**
     * bind lua functions
     *
     * @param factory
     * @param methods
     * @return
     */
    public static LuaTable bind(Class<? extends LibFunction> factory, List<Method> methods) {
        LuaTable env = new LuaTable();
        try {
            if (methods != null) {
                for (int i = 0; i < methods.size(); i++) {
                    LibFunction f = factory.newInstance();
                    f.method = methods.get(i);
                    f.name = methods.get(i).getName();
                    env.set(f.name, f);
                }
            }
        } catch (Exception e) {
            throw new LuaError("[Bind Failed] " + e);
        } finally {
            return env;
        }
    }

}
