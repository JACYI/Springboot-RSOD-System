# BS

## 介绍
SpringBoot + Vue + MySQL


## git使用说明

**分支情况**

![输入图片说明](images/git_1.png)

main：主分支，连接远程主分支，**不能修改**，只能pull和push，

yiyonghao：开发人员分支，git commit上传到这个分支，使用该分支进行开发。

remotes/origin/HEAD -> origin/main，远程分支和本地最新仓库连接


**开发过程**

```shell
# 1. 创建本地新分支，并切换到该新分支
git checkout -b yiyonghao
# 若已经存在，则切换到该分支即可
git checkout yiyonghao

# 2. 开发过程。。。


# 3. 上传开发代码到本地开发人员分支
git add .
git commit -m "this is a message"

# 4. 切换主分支，!!!先合并其他人上传的新代码!!!!
git checkout main
git pull --rebase

# 5. 合并本地开发分支和主分支，并上传新代码
git merge yiyonghao
git push
```

## IDEA 开发使用说明

参考文档：
- https://blog.csdn.net/jdsaiasodh/article/details/124667680
- https://blog.csdn.net/weixin_43252521/article/details/123959391


 **1.1 创建本地新分支** 

![输入图片说明](images/git_checkout.png.png)


 **1.2 切换分支** 

![输入图片说明](images/git_branch1.png)

 **2. commit新代码** 

选择commit的文件，填写commit message，并点击commit， **不要点击commit & push** 。

![输入图片说明](images/git_commit.png)

 **3. 合并主分支和本地开发分支** 

！！！合并之前注意检查，主分支是否与远程分支同步！！！

选择开发分支

![输入图片说明](images/git_merge.png)

 **4. push** 

![输入图片说明](images/git_push.png)

 **5. reset**
若忘记同步远程代码就merge新的修改代码，且未push到远程，可以使用reset，使用```HEAD^```表示回退到上一个版本。

![输入图片说明](images/git_reset.png) 

![输入图片说明](images/git_reset_2.png)

在回退后使用pull合并远程后，再merge。



## 开发记录

### Backend

**UserContoller login/logout**: 缺少对session的处理，返回值是boolean，缺少返回注册成功的id号的逻辑




#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request


#### 特技

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5.  Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
