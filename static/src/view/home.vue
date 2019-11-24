<template>
  <div class="index">
    <Header></Header>
    <LeftMenu></LeftMenu>
    <div class="rightContainer" :class="{'content-collapse':collapse}">
      <Tags/>
      <div class="content">
        <transition name="move" mode="out-in">
          <keep-alive :include="tagsList">
            <router-view></router-view>
          </keep-alive>
        </transition>
      </div>
    </div>
  </div>
</template>

<script>

    import Header from "../components/Header";
    import LeftMenu from "../components/LeftMenu";
    import bus from "../components/bus";
    import Tags from "../components/Tags";

    export default {
        name: "Index",
        data() {
            return {
                tagsList: [],
                collapse: false
            }
        },
        components: {
            Header,
            LeftMenu,
            Tags
        },
        created() {
            //内容区域跟随变化
            bus.$on("collapse", msg => {
                console.log(msg);
                this.collapse = msg;
            });
            //只有在标签页列表里的页面才能使用keep-alivee,
            //即关闭标签之后就不保存到列中了
            bus.$on("tag", msg => {
                //打开标签的数组
                let arr = [];
                msg.forEach(function (val, indx, arrself) {
                    val.name && arr.push(val.name);
                });
                this.tagsList = arr;
            })
        }
        // methods: {
        //     onSubmit() {
        //
        //     },
        //     handleClose() {
        //
        //     },
        //     handleOpen() {
        //
        //     },
        //     handleSelect() {
        //
        //     },
        //     //退出登录
        //     logout: function () {
        //         let _this = this;
        //         this.$confirm('确认退出吗？', '提示', {
        //             //取消则不做动作
        //         }).then(() => {
        //             localStorage.removeItem('Authorization');
        //             _this.$router.push('/login');
        //         }).catch(() => {
        //             //报错则不做动作
        //         });
        //     },
        //     collapse: function () {
        //         this.collapsed = !this.collapsed;
        //     },
        //     showMenu(i, status) {
        //         this.$refs.menuCollapsed.getElementsByClassName('submenu-hook-' + i)[0].style.display = status ? 'block' : 'none';
        //     }
        // }, mounted() {
        //     let sysUser = localStorage.getItem('sysUser');
        //     if (sysUser) {
        //         sysUser = JSON.parse(sysUser);
        //         this.sysName = sysUser.name || '';
        //         this.sysUserAvatar = sysUser.sysUserAvatar || '';
        //     }
        // }
        // name: ""
    }
</script>

<style scoped>
  .index {
    width: 100%;
    height: 100%;
    overflow: hidden;
  }

  .content {
    width: auto;
    height: 100%;
    padding: 10px;
    overflow-y: scroll;
    box-sizing: border-box;
  }

  .rightContainer.content-collapse {
    left: 48px;
  }

  .rightContainer {
    position: absolute;
    left: 180px;
    right: 0;
    top: 72px;
    height: 100%;
    overflow-y: scroll;
    padding-bottom: 30px;
    transition: left 0.3s ease-in-out;
  }

</style>
