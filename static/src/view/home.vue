<template>

  <el-row class="container">
    <!--栅格系统布局方法，布局头-->
    <el-col :span="24" class="header">
      <el-col :span="10" class="logo" :class="collapsed?'logo-collapse-width':'logo-width'">
        {{collapsed?'':sysName}}
      </el-col>
      <el-col :span="4" class="userinfo">
        <el-dropdown trigger="hover">
          <span class="el-dropdown-link userinfo-inner">
<!--            <img src="">-->
            {{sysUserName}}
          </span>
          <el-dropdown-sysMenu slot="dropdown">
            <el-dropdown-item>我的消息</el-dropdown-item>
            <el-dropdown-item>设置</el-dropdown-item>
            <el-dropdown-item divided @click.native="logout">退出登录</el-dropdown-item>
          </el-dropdown-sysMenu>
        </el-dropdown>
      </el-col>
    </el-col>

    <!--栅格系统体-->
    <el-col :span="24" class="main">
      <!--侧边栏-->
      <aside :class="collapsed?'sysMenu-collapsed':'sysMenu-expanded'">
        <el-sysMenu :default-active="$route.path" class="el-sysMenu-vertical-demo" @open="handleOpen" @close="handleClose"
                 @select="handleSelect"
                 unique-opened router v-show="!collapsed">
          <template v-for="(item,index) in $router.options.routes" v-if="!item.hidden">
            <el-submenu :index="index+''" v-if="!item.leaf">
              <template slot="title"><i :class="item.iconCls"></i>{{item.name}}</template>
              <el-sysMenu-item v-for="child in item.children" :index="child.path" :key="child.path" v-if="!child.hidden">
                {{child.name}}
              </el-sysMenu-item>
            </el-submenu>
            <el-sysMenu-item v-if="item.leaf&&item.children.length>0" :index="item.children[0].path"><i
              :class="item.iconCls"></i>{{item.children[0].name}}
            </el-sysMenu-item>
          </template>
        </el-sysMenu>
      </aside>

      <section class="content-container">
        <div class="grid-content bg-purple-light">
          <el-col :span="24" class="breadcrumb-container">
            <strong class="title">{{$route.name}}</strong>
            <el-breadcrumb separator="/" class="breadcrumb-inner">
              <el-breadcrumb-item v-for="item in $route.matched" :key="item.path">
                {{ item.name }}
              </el-breadcrumb-item>
            </el-breadcrumb>
          </el-col>
          <el-col :span="24" class="content-wrapper">
            <transition name="fade" mode="out-in">
              <router-view></router-view>
            </transition>
          </el-col>
        </div>
      </section>

    </el-col>
  </el-row>
</template>

<script>
    export default {
        data() {
            return {
                sysName: '',
                collapsed: false,
                sysUserName: '',
                sysUserAvatar: '',
                from: {
                    name: '',
                    region: '',

                }
            }
        },
        methods: {
            onSubmit() {

            },
            handleClose() {

            },
            handleOpen() {

            },
            handleSelect() {

            },
            //退出登录
            logout: function () {
                let _this = this;
                this.$confirm('确认退出吗？', '提示', {
                    //取消则不做动作
                }).then(() => {
                    localStorage.removeItem('Authorization');
                    _this.$router.push('/login');
                }).catch(() => {
                    //报错则不做动作
                });
            },
            collapse: function () {
                this.collapsed = !this.collapsed;
            },
            showMenu(i, status) {
                this.$refs.menuCollapsed.getElementsByClassName('submenu-hook-' + i)[0].style.display = status ? 'block' : 'none';
            }
        }, mounted() {
            let sysUser = localStorage.getItem('sysUser');
            if (sysUser) {
                sysUser = JSON.parse(sysUser);
                this.sysName = sysUser.name || '';
                this.sysUserAvatar = sysUser.sysUserAvatar || '';
            }
        }
        // name: ""
    }
</script>

<style scoped>


  .container {
    /*position: absolute;*/
    top: 0;
    bottom: 0;
    width: 100%;
  }

  .header {
    height: 60px;
    /*line-height: 60px;*/
    /*border-bottom: 5px solid $color-primary;*/
    background: #66ccff;
    padding: 35px 35px 15px 35px;
    box-shadow: 0 0 25px #cac6c6;
  }

  .userinfo {
    text-align: right;
    padding-right: 35px;
    float: right;
  }

  .userinfo-inner {
    cursor: pointer;
    /*$color-primary;*/
  }

  img {
    width: 40px;
    height: 40px;
    border-radius: 20px;
    margin: 10px 0px 10px 10px;
    float: right;
  }

  .logo {
    /*width: 230 px;*/
    height: 160px;
    font-size: 22px;
    padding-left: 20px;
    padding-right: 20px;
    border-color: #f0f0f0;
    border-right-width: 1px;
    border-right-style: solid;
    /*background: url("http://img4.duitang.com/uploads/item/201511/26/20151126112617_vUaQf.jpeg") no-repeat center;*/
    background-size: 60%;
    background-color: #ffffff;
  }

  img {
    width: 40px;
    float: left;
    margin: 10px 10px 10px 18px;
  }

  .txt {
    color: #fff;
  }

  .logo-width {
    width: 230px;
  }

  .logo-collapse-width {
    width: 60px
  }

  .tools {
    padding: 0px 23px;
    width: 14px;
    height: 60px;
    line-height: 60px;
    cursor: pointer;
  }

  .main {
    display: flex;
    /*background: #324057;*/
    position: absolute;
    top: 60px;
    bottom: 0px;
    overflow: hidden;
  }

  aside {
    flex: 0 0 230px;
    width: 230px;
    position: absolute;
    /*top: 0 px;*/
    /*bottom: 0 px;*/
  }

  .el-sysMenu {
    height: 100%;
  }

  .collapsed {
    width: 60px;
  }

  .item {
    position: relative;
  }

  .submenu {
    position: absolute;
    top: 0px;
    left: 60px;
    z-index: 99999;
    height: auto;
    display: none;
  }

  .sysMenu-collapsed {
    flex: 0 0 60px;
    width: 60px;
  }

  .sysMenu-expanded {
    flex: 0 0 230px;
    width: 230px;
    padding-top: 100px;
  }

  .content-container {
    /*background: #f1f2f7;*/
    flex: 1;
    /*position: absolute;*/
    /*right: 0 px;*/
    /*top: 0 px;*/
    /*bottom: 0 px;*/
    /*left: 230 px;*/
    overflow-y: scroll;
    padding: 20px;
  }

  .breadcrumb-container {
    /*margin-bottom: 15 px;*/
  }

  .title {
    width: 200px;
    float: left;
    color: #475669;
  }

  .breadcrumb-inner {
    float: right;
  }

  .content-wrapper {
    background-color: #fff;
    box-sizing: border-box;
  }

</style>
