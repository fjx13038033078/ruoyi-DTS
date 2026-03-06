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
          <el-button type="primary" plain @click="$router.push('/front/settings')">
            <i class="el-icon-setting"></i> 账号设置
          </el-button>
          <el-button type="success" plain @click="$router.push('/front/points')">
            <i class="el-icon-coin"></i> 积分商城
          </el-button>
        </div>
        <el-divider></el-divider>

        <el-tabs v-model="activeTab">
          <el-tab-pane label="我的订单" name="orders">
            <div v-loading="orderLoading" class="tab-content">
              <div v-if="orderList.length === 0 && !orderLoading" class="empty-tip">暂无订单</div>
              <div v-else v-for="item in orderList" :key="item.orderId" class="order-item">
                <span class="order-no">{{ item.orderNo }}</span>
                <span class="order-amount">¥{{ item.totalAmount }}</span>
                <el-tag v-if="item.status === '0'" type="warning" size="small">待支付</el-tag>
                <el-tag v-else-if="item.status === '1'" type="success" size="small">已支付</el-tag>
                <el-button v-if="item.status === '0'" type="primary" size="mini" @click="goPay(item.orderNo)">去支付</el-button>
              </div>
            </div>
          </el-tab-pane>

          <el-tab-pane label="我的收藏" name="favorites">
            <div v-loading="favoriteLoading" class="tab-content">
              <div v-if="favoriteList.length === 0 && !favoriteLoading" class="empty-tip">暂无收藏</div>
              <div v-else class="favorite-grid">
                <div
                  v-for="item in favoriteList"
                  :key="item.performanceId"
                  class="favorite-card"
                  @click="$router.push('/front/detail/' + item.performanceId)"
                >
                  <div class="card-poster">
                    <image-preview v-if="item.posterUrl" :src="item.posterUrl" :width="120" :height="90" />
                    <div v-else class="poster-placeholder"><i class="el-icon-video-play"></i></div>
                  </div>
                  <div class="card-info">
                    <span class="card-title">{{ item.title }}</span>
                    <el-button type="text" size="mini" @click.stop="removeFavorite(item)">取消收藏</el-button>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>

          <el-tab-pane label="我的积分与兑换记录" name="points">
            <div v-loading="pointsLoading" class="tab-content">
              <div class="points-summary">
                <span class="points-label">当前积分：</span>
                <span class="points-value">{{ pointsDisplay }}</span>
              </div>
              <h4 class="record-title">兑换记录</h4>
              <div v-if="recordList.length === 0 && !pointsLoading" class="empty-tip">暂无兑换记录</div>
              <el-table v-else :data="recordList" stripe size="small" class="record-table">
                <el-table-column prop="createTime" label="兑换时间" width="180">
                  <template slot-scope="scope">
                    {{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}
                  </template>
                </el-table-column>
                <el-table-column prop="itemName" label="商品名称" min-width="120">
                  <template slot-scope="scope">
                    {{ scope.row.itemName || '未知商品' }}
                  </template>
                </el-table-column>
                <el-table-column prop="pointsDeducted" label="扣除积分" width="100">
                  <template slot-scope="scope">
                    <span class="points-deducted">-{{ scope.row.pointsDeducted || 0 }}</span>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
      <div v-else class="login-tip">
        <p>请先 <router-link to="/login">登录</router-link></p>
      </div>
    </el-card>
  </div>
</template>

<script>
import { parseTime } from '@/utils/ruoyi'
import { mapState } from 'vuex'
import { listMyOrders, alipayPay, listMyFavorites, removeFavorite as removeFavoriteApi } from '@/api/front/ticket'
import { listPointsRecords } from '@/api/ticket/points'

export default {
  name: 'FrontProfile',
  data() {
    return {
      activeTab: 'orders',
      orderList: [],
      orderLoading: false,
      favoriteList: [],
      favoriteLoading: false,
      recordList: [],
      pointsLoading: false
    }
  },
  computed: {
    ...mapState({
      points: state => state.user.points
    }),
    isLogin() {
      return this.$store.getters.token
    },
    userId() {
      return this.$store.state.user.id || '-'
    },
    avatar() {
      return this.$store.getters.avatar
    },
    name() {
      return this.$store.getters.name
    },
    pointsDisplay() {
      return this.points != null ? this.points : 0
    }
  },
  watch: {
    activeTab(val) {
      if (val === 'points') this.loadRecords()
      if (val === 'favorites') this.loadFavorites()
    }
  },
  created() {
    if (this.isLogin) {
      this.loadOrders()
    }
  },
  methods: {
    loadOrders() {
      this.orderLoading = true
      listMyOrders({ pageNum: 1, pageSize: 20 }).then(res => {
        this.orderList = res.rows || []
        this.orderLoading = false
      }).catch(() => { this.orderLoading = false })
    },
    loadFavorites() {
      this.favoriteLoading = true
      listMyFavorites({ pageNum: 1, pageSize: 50 }).then(res => {
        this.favoriteList = res.rows || []
        this.favoriteLoading = false
      }).catch(() => { this.favoriteLoading = false })
    },
    removeFavorite(item) {
      removeFavoriteApi(item.performanceId).then(() => {
        this.$message.success('已取消收藏')
        this.favoriteList = this.favoriteList.filter(p => p.performanceId !== item.performanceId)
      }).catch(() => {})
    },
    loadRecords() {
      this.pointsLoading = true
      listPointsRecords({ pageNum: 1, pageSize: 50 }).then(res => {
        this.recordList = res.rows || []
        this.pointsLoading = false
      }).catch(() => { this.pointsLoading = false })
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
    },
    parseTime
  }
}
</script>

<style lang="scss" scoped>
.front-profile {
  .profile-card {
    max-width: 700px;
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
  .tab-content {
    min-height: 120px;
  }
  .empty-tip {
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
  .points-summary {
    margin-bottom: 16px;
    font-size: 16px;
    .points-label { color: #666; }
    .points-value { font-weight: bold; color: #3949ab; }
  }
  .record-title {
    margin: 0 0 12px 0;
    font-size: 15px;
    color: #333;
  }
  .record-table {
    margin-top: 8px;
    .points-deducted { color: #f44336; }
  }
  .favorite-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
    gap: 16px;
  }
  .favorite-card {
    display: flex;
    gap: 12px;
    padding: 12px;
    background: #fafafa;
    border-radius: 8px;
    cursor: pointer;
    transition: background 0.2s;
    &:hover { background: #f0f0f0; }
    .card-poster {
      flex-shrink: 0;
      width: 120px;
      height: 90px;
      border-radius: 6px;
      overflow: hidden;
      background: #e8eaf6;
      display: flex;
      align-items: center;
      justify-content: center;
      .poster-placeholder { color: #9fa8da; font-size: 28px; }
    }
    .card-info {
      flex: 1;
      min-width: 0;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      .card-title {
        font-size: 14px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
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
