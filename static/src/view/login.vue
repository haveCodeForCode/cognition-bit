<template>
  <el-form :model="ruleForm2" :rules="rules2" ref="ruleForm2" label-position="left" label-width="0px"
           class="demo-ruleForm login-container">
    <h4 class="title">登录</h4>

    <el-form-item prop="account">
      <el-input type="text" v-model="ruleForm2.account" auto-complete="off" placeholder="账号"></el-input>
    </el-form-item>

    <el-form-item prop="checkPass">
      <el-input type="password" v-model="ruleForm2.pass" auto-complete="off" placeholder="密码"></el-input>
    </el-form-item>

    <!--    <el-checkbox v-model="checked" checked class="remember">记住密码</el-checkbox>-->

    <el-link type="primary" href="" class="register">前往注册>>></el-link>

    <el-form-item style="width:100%;">
      <el-button type="primary" style="width:100%;" @click="submitForm('ruleForm2')">
        <!--                 @click.native.prevent="handleSubmit2" :loading="logining">-->
        登录
      </el-button>
      <!--<el-button @click.native.prevent="handleReset2">重置</el-button>-->
    </el-form-item>
  </el-form>

</template>

<script>
    import {mapMutations} from 'vuex';
    import Qs from 'qs';

    export default {
        name: 'login',
        data() {
            let validatePass = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入密码'))
                } else {
                    /*if (this.ruleForm2.pass !== '') {
                      this.$refs.ruleForm2.validateField('pass')
                    }*/
                    callback()
                }
            };
            let validateAccount = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入账户名'))
                } else {
                    /* if (this.ruleForm2.account !== '') {
                       this.$refs.ruleForm2.validateField('account')
                     }*/
                    callback()
                }
            };
            return {
                userToken: "",
                ruleForm2: {
                    pass: '',
                    account: '',
                },
                rules2: {
                    pass: [
                        {validator: validatePass, trigger: 'blur'}
                    ],
                    account: [
                        {validator: validateAccount, trigger: 'blur'}
                    ]
                }
            }
        },
        methods: {
            ...mapMutations(['changeLogin']),
            submitForm(formName) {
                let _this = this;
                let data = {
                    'loginInfo': this.ruleForm2.account,
                    'password': this.ruleForm2.pass
                };
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        console.log(valid);
                        this.$axios({
                            method: 'post',
                            url: this.api + '/login',
                            data: Qs.stringify(data),
                            headers: {
                                "Authorization": " "
                            }
                        }).then(res => {  //res是返回结果
                            console.log(res);
                            //判断是否成功
                            if (res.data.success) {
                                _this.userToken = 'Bearer ' + res.data.data;
                                _this.changeLogin({Authorization: _this.userToken});
                                this.$message("登录成功！");
                                this.$router.push('/home');
                            } else {
                                this.$message(res.data.msg);
                                return false;
                            }
                            // //获取到的token
                            // console.log(this.userToken);
                        }).catch(err => { //请求失败就会捕获报错信息
                            this.$message("服务器连接失败");
                            console.log(err);
                        })
                    } else {
                        this.$message("用户填写信息错误");
                        return false;
                    }
                })
            },
            resetForm(formName) {
                this.$refs[formName].resetFields()
            },
            register() {
                this.$router.push('/register')
            }
        }
    }
</script>

<style scoped>
  .login-container {
    -webkit-border-radius: 5px;
    border-radius: 5px;
    -moz-border-radius: 5px;
    background-clip: padding-box;
    margin: 140px auto;
    width: 350px;
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }

  .title {
    margin: 0 auto 40px auto;
    text-align: center;
    color: #505458;
  }

  .register {
    margin: 0 0 10px 250px;
  }
</style>
