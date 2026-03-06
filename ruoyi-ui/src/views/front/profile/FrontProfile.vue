<template>
  <div class="front-profile">
    <el-card class="profile-card">
      <div slot="header" class="card-header">
        <span><i class="el-icon-user"></i> 个人中心</span>
      </div>
      <div class="profile-content" v-if="isLogin">
        <div class="avatar-section">
          <img :src="avatar" class="user-avatar" />
          <div class="user-info">
            <h3>{{ name }}</h3>
            <p class="user-id">用户ID: {{ userId }}</p>
          </div>
        </div>
        <el-divider></el-divider>
        <div class="actions">
          <el-button type="primary" plain @click="$router.push('/user/profile')">
            <i class="el-icon-setting"></i> 账号设置
          </el-button>
        </div>
      </div>
      <div v-else class="login-tip">
        <p>请先 <router-link to="/login">登录</router-link></p>
      </div>
    </el-card>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'FrontProfile',
  computed: {
    ...mapGetters(['avatar', 'name', 'roles']),
    isLogin() {
      return this.$store.getters.token
    },
    userId() {
      return this.$store.state.user.id || '-'
    }
  }
}
</script>

<style lang="scss" scoped>
.front-profile {
  .profile-card {
    max-width: 500px;
    margin: 0 auto;
  }
  .card-header {
    font-size: 18px;
    font-weight: 500;
  }
  .avatar-section {
    display: flex;
    align-items: center;
    gap: 20px;
    .user-avatar {
      width: 80px;
      height: 80px;
      border-radius: 50%;
      border: 3px solid #e0e0e0;
    }
    .user-info h3 {
      margin: 0 0 8px 0;
      font-size: 20px;
    }
    .user-id {
      margin: 0;
      color: #999;
      font-size: 14px;
    }
  }
  .login-tip {
    text-align: center;
    padding: 40px;
    color: #666;
    a {
      color: #409eff;
    }
  }
}
</style>
