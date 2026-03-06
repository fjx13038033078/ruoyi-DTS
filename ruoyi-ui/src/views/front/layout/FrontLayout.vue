<template>
  <div class="front-layout">
    <!-- 顶部导航栏 -->
    <header class="front-header">
      <div class="header-container">
        <div class="logo" @click="$router.push('/front')">
          <span class="logo-title">音乐剧票务系统</span>
        </div>
        <nav class="nav-menu">
          <router-link to="/front" class="nav-item" exact>
            <i class="el-icon-s-home"></i> 首页
          </router-link>
          <router-link to="/front/performance" class="nav-item">
            <i class="el-icon-video-play"></i> 演出列表
          </router-link>
          <router-link to="/front/points" class="nav-item" v-if="isLogin">
            <i class="el-icon-coin"></i> 积分商城
          </router-link>
          <router-link to="/front/profile" class="nav-item" v-if="isLogin">
            <i class="el-icon-user"></i> 个人中心
          </router-link>
        </nav>
        <div class="user-info">
          <template v-if="isLogin">
            <el-dropdown @command="handleCommand">
              <span class="user-dropdown">
                <img :src="avatar" class="user-avatar" />
                <span class="user-name">{{ name }}</span>
                <i class="el-icon-arrow-down"></i>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="profile">
                  <i class="el-icon-user"></i> 个人中心
                </el-dropdown-item>
                <el-dropdown-item command="settings">
                  <i class="el-icon-setting"></i> 账号设置
                </el-dropdown-item>
                <el-dropdown-item command="admin" v-if="isAdmin">
                  <i class="el-icon-setting"></i> 管理后台
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">
                  <i class="el-icon-switch-button"></i> 退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
          <template v-else>
            <router-link to="/login" class="login-btn">登录</router-link>
            <router-link to="/register" class="register-btn" v-if="register">注册</router-link>
          </template>
        </div>
      </div>
    </header>

    <!-- 主内容区 -->
    <main class="front-main">
      <router-view />
    </main>

    <!-- 底部 -->
    <footer class="front-footer">
      <div class="footer-container">
        <p>音乐剧票务管理系统 &copy; {{ new Date().getFullYear() }}</p>
        <p>精彩演出 · 尽在掌握</p>
      </div>
    </footer>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'FrontLayout',
  data() {
    return {
      register: false
    }
  },
  computed: {
    ...mapGetters(['avatar', 'name', 'roles']),
    isLogin() {
      return this.$store.getters.token
    },
    isAdmin() {
      return this.roles && (
        this.roles.includes('admin') ||
        this.roles.includes('merchants') ||
        this.roles.some(r => (r || '').toLowerCase().includes('admin')) ||
        this.roles.some(r => (r || '').toLowerCase().includes('merchants'))
      )
    }
  },
  methods: {
    handleCommand(command) {
      switch (command) {
        case 'profile':
          this.$router.push('/front/profile')
          break
        case 'settings':
          this.$router.push('/front/settings')
          break
        case 'admin':
          this.$router.push('/index')
          break
        case 'logout':
          this.$store.dispatch('LogOut').then(() => {
            location.href = '/front'
          })
          break
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.front-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f5f5;
}

.front-header {
  background: linear-gradient(135deg, #1a237e 0%, #283593 50%, #3949ab 100%);
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.2);
  position: sticky;
  top: 0;
  z-index: 100;

  .header-container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 20px;
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .logo {
    cursor: pointer;
    .logo-title {
      font-size: 22px;
      font-weight: bold;
      color: #fff;
      letter-spacing: 2px;
    }
  }

  .nav-menu {
    display: flex;
    gap: 8px;
    .nav-item {
      color: rgba(255, 255, 255, 0.9);
      text-decoration: none;
      padding: 8px 16px;
      border-radius: 4px;
      transition: all 0.3s ease;
      font-size: 15px;
      display: flex;
      align-items: center;
      gap: 6px;
      &:hover {
        background-color: rgba(255, 255, 255, 0.15);
      }
      &.router-link-active {
        background-color: rgba(255, 255, 255, 0.25);
        font-weight: 500;
      }
    }
  }

  .user-info {
    display: flex;
    align-items: center;
    gap: 15px;
    .user-dropdown {
      display: flex;
      align-items: center;
      cursor: pointer;
      color: #fff;
      .user-avatar {
        width: 32px;
        height: 32px;
        border-radius: 50%;
        margin-right: 8px;
        border: 2px solid rgba(255, 255, 255, 0.8);
      }
    }
    .login-btn, .register-btn {
      padding: 6px 16px;
      border-radius: 4px;
      text-decoration: none;
      font-size: 14px;
      transition: all 0.3s ease;
    }
    .login-btn {
      color: #fff;
      border: 1px solid rgba(255, 255, 255, 0.8);
      &:hover {
        background-color: rgba(255, 255, 255, 0.2);
      }
    }
    .register-btn {
      background-color: rgba(255, 255, 255, 0.9);
      color: #1a237e;
      &:hover {
        background-color: #fff;
      }
    }
  }
}

.front-main {
  flex: 1;
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px 20px;
  width: 100%;
  box-sizing: border-box;
}

.front-footer {
  background: #1a237e;
  color: rgba(255, 255, 255, 0.8);
  padding: 24px 20px;
  text-align: center;
  .footer-container p {
    margin: 6px 0;
    font-size: 13px;
  }
}
</style>
