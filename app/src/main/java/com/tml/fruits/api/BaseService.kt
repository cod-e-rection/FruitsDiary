package com.tml.fruits.api

import androidx.fragment.app.Fragment
import java.lang.ref.WeakReference

// extended base service
open class BaseService constructor(activity: Fragment) {

    protected var weakReference: WeakReference<Fragment> = WeakReference(activity)

}