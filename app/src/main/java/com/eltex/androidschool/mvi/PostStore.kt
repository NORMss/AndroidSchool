package com.eltex.androidschool.mvi

import com.eltex.androidschool.view.model.PostUi
import com.eltex.androidschool.view.fragment.post.PostEffect
import com.eltex.androidschool.view.fragment.post.PostMessage

typealias PostStore = Store<PostUi, PostMessage, PostEffect>