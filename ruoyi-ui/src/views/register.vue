<template>
  <div class="auth-page">
    <div class="auth-card">
      <!-- 左侧：磨玻璃装饰区 -->
      <div class="auth-card__glass">
        <div class="glass-content">
          <h1 class="glass-title">音乐剧票务</h1>
          <p class="glass-subtitle">精彩演出 · 尽在掌握</p>
          <div class="glass-decoration">
            <i class="el-icon-video-play"></i>
          </div>
        </div>
      </div>
      <!-- 右侧：表单区 -->
      <div class="auth-card__form">
        <div class="form-inner">
          <h3 class="form-title">注 册</h3>
          <el-form ref="registerForm" :model="registerForm" :rules="registerRules" class="register-form">
            <el-form-item prop="username">
              <el-input v-model="registerForm.username" type="text" auto-complete="off" placeholder="账号">
                <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
              </el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input
                v-model="registerForm.password"
                type="password"
                auto-complete="off"
                placeholder="密码"
                @keyup.enter.native="handleRegister"
              >
                <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
              </el-input>
            </el-form-item>
            <el-form-item prop="confirmPassword">
              <el-input
                v-model="registerForm.confirmPassword"
                type="password"
                auto-complete="off"
                placeholder="确认密码"
                @keyup.enter.native="handleRegister"
              >
                <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
              </el-input>
            </el-form-item>
            <el-form-item prop="code" v-if="captchaEnabled">
              <el-input
                v-model="registerForm.code"
                auto-complete="off"
                placeholder="验证码"
                style="width: 63%"
                @keyup.enter.native="handleRegister"
              >
                <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
              </el-input>
              <div class="register-code">
                <img :src="codeUrl" @click="getCode" class="register-code-img"/>
              </div>
            </el-form-item>
            <el-form-item style="width:100%;">
              <el-button
                :loading="loading"
                size="medium"
                type="primary"
                class="auth-btn"
                @click.native.prevent="handleRegister"
              >
                <span v-if="!loading">注 册</span>
                <span v-else>注 册 中...</span>
              </el-button>
              <div class="form-footer">
                <router-link class="link-type" :to="'/login'">使用已有账户登录</router-link>
              </div>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getCodeImg, register } from "@/api/login";

export default {
  name: "Register",
  data() {
    const equalToPassword = (rule, value, callback) => {
      if (this.registerForm.password !== value) {
        callback(new Error("两次输入的密码不一致"));
      } else {
        callback();
      }
    };
    return {
      codeUrl: "",
      registerForm: {
        username: "",
        password: "",
        confirmPassword: "",
        code: "",
        uuid: ""
      },
      registerRules: {
        username: [
          { required: true, trigger: "blur", message: "请输入您的账号" },
          { min: 2, max: 20, message: '用户账号长度必须介于 2 和 20 之间', trigger: 'blur' }
        ],
        password: [
          { required: true, trigger: "blur", message: "请输入您的密码" },
          { min: 5, max: 20, message: '用户密码长度必须介于 5 和 20 之间', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, trigger: "blur", message: "请再次输入您的密码" },
          { required: true, validator: equalToPassword, trigger: "blur" }
        ],
        code: [{ required: true, trigger: "change", message: "请输入验证码" }]
      },
      loading: false,
      captchaEnabled: true
    };
  },
  created() {
    this.getCode();
  },
  methods: {
    getCode() {
      getCodeImg().then(res => {
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled;
        if (this.captchaEnabled) {
          this.codeUrl = "data:image/gif;base64," + res.img;
          this.registerForm.uuid = res.uuid;
        }
      });
    },
    handleRegister() {
      this.$refs.registerForm.validate(valid => {
        if (valid) {
          this.loading = true;
          register(this.registerForm).then(res => {
            const username = this.registerForm.username;
            this.$alert("<font color='red'>恭喜你，您的账号 " + username + " 注册成功！</font>", '系统提示', {
              dangerouslyUseHTMLString: true,
              type: 'success'
            }).then(() => {
              this.$router.push("/login");
            }).catch(() => {});
          }).catch(() => {
            this.loading = false;
            if (this.captchaEnabled) {
              this.getCode();
            }
          })
        }
      });
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.auth-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-image: url("../assets/images/login-background.jpg");
  background-size: cover;
  background-position: center;
}

.auth-card {
  display: flex;
  width: 720px;
  min-height: 480px;
  overflow: hidden;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  @media (max-width: 768px) {
    flex-direction: column;
    width: 90%;
    max-width: 400px;
    min-height: auto;
  }
}

.auth-card__glass {
  flex: 1;
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border-right: 1px solid rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
  @media (max-width: 768px) {
    border-right: none;
    border-bottom: 1px solid rgba(255, 255, 255, 0.2);
    padding: 24px;
  }
}

.glass-content {
  text-align: center;
  color: #fff;
}

.glass-title {
  font-size: 28px;
  font-weight: 600;
  margin: 0 0 12px 0;
  letter-spacing: 4px;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}

.glass-subtitle {
  font-size: 15px;
  margin: 0 0 32px 0;
  opacity: 0.95;
  letter-spacing: 2px;
}

.glass-decoration {
  font-size: 64px;
  opacity: 0.9;
}

.auth-card__form {
  flex: 1;
  background: rgba(255, 255, 255, 0.25);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
}

.form-inner {
  width: 100%;
  max-width: 280px;
}

.form-title {
  margin: 0 0 28px 0;
  font-size: 22px;
  font-weight: 600;
  color: #333;
  text-align: center;
  letter-spacing: 6px;
}

.register-form {
  .el-input {
    height: 42px;
    .el-input__inner {
      background: rgba(255, 255, 255, 0.7);
      border: 1px solid rgba(255, 255, 255, 0.5);
      height: 42px;
    }
    input { height: 42px; }
  }
  .input-icon {
    height: 42px;
    width: 16px;
    margin-left: 2px;
  }
}

.auth-btn {
  width: 100%;
  height: 42px;
  font-size: 16px;
  letter-spacing: 4px;
  background: linear-gradient(135deg, #3949ab 0%, #5c6bc0 100%);
  border: none;
  &:hover { background: linear-gradient(135deg, #303f9f 0%, #3949ab 100%); }
}

.form-footer {
  text-align: right;
  margin-top: 12px;
  .link-type {
    color: #5c6bc0;
    font-size: 13px;
    text-decoration: none;
    &:hover { color: #3949ab; text-decoration: underline; }
  }
}

.register-code {
  width: 33%;
  height: 42px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
    height: 42px;
  }
}
</style>
