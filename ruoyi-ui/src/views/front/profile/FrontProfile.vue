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
        <el-divider></el-divider>
        <div class="order-section">
          <h4><i class="el-icon-ticket"></i> 我的订单</h4>
          <div v-loading="orderLoading" class="order-list">
            <div v-if="orderList.length === 0 && !orderLoading" class="order-empty">暂无订单</div>
            <div v-else v-for="item in orderList" :key="item.orderId" class="order-item">
              <span class="order-no">{{ item.orderNo }}</span>
              <span class="order-amount">¥{{ item.totalAmount }}</span>
              <el-tag v-if="item.status === '0'" type="warning" size="small">待支付</el-tag>
              <el-tag v-else-if="item.status === '1'" type="success" size="small">已支付</el-tag>
              <el-button v-if="item.status === '0'" type="primary" size="mini" @click="goPay(item.orderNo)">去支付</el-button>
            </div>
          </div>
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
import { listMyOrders, alipayPay } from '@/api/front/ticket'

export default {
  name: 'FrontProfile',
  data() {
    return {
      orderList: [],
      orderLoading: false
    }
  },
  computed: {
    ...mapGetters(['avatar', 'name', 'roles']),
    isLogin() {
      return this.$store.getters.token
    },
    userId() {
      return this.$store.state.user.id || '-'
    }
  },
  created() {
    if (this.isLogin) this.loadOrders()
  },
  methods: {
    loadOrders() {
      this.orderLoading = true
      listMyOrders({ pageNum: 1, pageSize: 20 }).then(res => {
        this.orderList = res.rows || []
        this.orderLoading = false
      }).catch(() => { this.orderLoading = false })
    },
    goPay(orderNo) {
      alipayPay(orderNo).then(html => {
        const div = document.createElement('div')
        div.innerHTML = html
        document.body.appendChild(div)
        const form = div.querySelector('form')
        if (form) {
          form.submit()
        } else {
          document.body.removeChild(div)
          this.$message.error('支付表单加载失败')
        }
      }).catch(() => {})
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
  .order-section {
    h4 {
      margin: 0 0 16px 0;
      font-size: 16px;
      color: #333;
    }
    .order-list {
      min-height: 80px;
    }
    .order-empty {
      text-align: center;
      padding: 24px;
      color: #999;
      font-size: 14px;
    }
    .order-item {
      display: flex;
      align-items: center;
      gap: 12px;
      padding: 12px 0;
      border-bottom: 1px solid #f0f0f0;
      font-size: 14px;
      &:last-child { border-bottom: none; }
      .order-no { flex: 1; color: #333; }
      .order-amount { color: #f44336; font-weight: 500; }
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
