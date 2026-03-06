<template>
  <div class="points-mall">
    <template v-if="isLogin">
    <section class="points-header">
      <div class="points-balance-card">
        <div class="balance-main">
          <span class="balance-label">我的积分</span>
          <span class="balance-value">{{ pointsDisplay }}</span>
        </div>
        <el-alert
          v-if="pointsLow"
          title="积分不足"
          type="warning"
          :closable="false"
          show-icon
          class="points-alert"
        >
          您的积分较少，部分商品可能无法兑换，请通过购票等方式获取更多积分。
        </el-alert>
      </div>
    </section>

    <section class="items-section">
      <div class="section-header">
        <h2 class="section-title">
          <i class="el-icon-goods"></i>
          积分商城
        </h2>
      </div>
      <div class="items-grid" v-loading="loading">
        <template v-if="itemList.length > 0">
          <div v-for="item in itemList" :key="item.itemId" class="item-card">
            <div class="card-image">
              <div class="image-placeholder">
                <i class="el-icon-goods"></i>
                <span>积分商品</span>
              </div>
            </div>
            <div class="card-info">
              <h3 class="card-title">{{ item.name }}</h3>
              <div class="card-meta">
                <span class="points-required">{{ item.pointsRequired }} 积分</span>
                <span class="stock">库存 {{ item.stock || 0 }}</span>
              </div>
              <el-button
                type="primary"
                size="small"
                class="exchange-btn"
                :disabled="!canExchange(item)"
                @click.stop="handleExchange(item)"
              >
                立即兑换
              </el-button>
            </div>
          </div>
        </template>
        <template v-else-if="!loading">
          <div class="empty-tip">
            <i class="el-icon-goods"></i>
            <p>暂无可兑换商品</p>
          </div>
        </template>
      </div>
      <div class="pagination-wrap" v-show="total > 0 && itemList.length > 0">
        <pagination
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="loadItems"
        />
      </div>
    </section>

    <el-dialog
      title="确认兑换"
      :visible.sync="confirmVisible"
      width="400px"
      append-to-body
    >
      <p v-if="selectedItem">
        确定要用 <strong>{{ selectedItem.pointsRequired }}</strong> 积分兑换「{{ selectedItem.name }}」吗？
      </p>
      <span slot="footer" class="dialog-footer">
        <el-button @click="confirmVisible = false">取消</el-button>
        <el-button type="primary" :loading="exchanging" @click="doExchange">确定</el-button>
      </span>
    </el-dialog>
    </template>
    <div v-else class="login-required">
      <el-alert title="请先登录" type="warning" :closable="false" show-icon>
        <template slot="default">
          积分商城需要登录后使用，请 <router-link to="/login">登录</router-link> 或 <router-link to="/register">注册</router-link>。
        </template>
      </el-alert>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import { listPointsItems, exchangePointsItem } from '@/api/ticket/points'
import { getInfo } from '@/api/login'

export default {
  name: 'PointsMall',
  data() {
    return {
      loading: false,
      itemList: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 12
      },
      confirmVisible: false,
      selectedItem: null,
      exchanging: false
    }
  },
  computed: {
    ...mapState({
      points: state => state.user.points
    }),
    isLogin() {
      return this.$store.getters.token
    },
    pointsDisplay() {
      return this.points != null ? this.points : 0
    },
    pointsLow() {
      const p = this.pointsDisplay
      return typeof p === 'number' && p < 100
    }
  },
  created() {
    this.loadItems()
  },
  methods: {
    loadItems() {
      this.loading = true
      listPointsItems(this.queryParams).then(res => {
        this.itemList = res.rows || []
        this.total = res.total || 0
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    canExchange(item) {
      const stock = item.stock != null ? item.stock : 0
      const required = item.pointsRequired != null ? item.pointsRequired : 0
      const myPoints = this.pointsDisplay
      return stock > 0 && myPoints >= required
    },
    handleExchange(item) {
      if (!this.canExchange(item)) return
      this.selectedItem = item
      this.confirmVisible = true
    },
    doExchange() {
      if (!this.selectedItem) return
      this.exchanging = true
      exchangePointsItem(this.selectedItem.itemId).then(() => {
        this.$message.success('兑换成功')
        this.confirmVisible = false
        this.selectedItem = null
        this.exchanging = false
        this.refreshUserAndList()
      }).catch(() => {
        this.exchanging = false
      })
    },
    refreshUserAndList() {
      this.$store.dispatch('GetInfo').then(() => {
        this.queryParams.pageNum = 1
        this.loadItems()
      }).catch(() => {
        this.queryParams.pageNum = 1
        this.loadItems()
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.points-mall {
  .points-header {
    margin-bottom: 30px;
  }

  .points-balance-card {
    background: linear-gradient(135deg, #3949ab 0%, #5c6bc0 100%);
    border-radius: 12px;
    padding: 24px 32px;
    color: #fff;
    box-shadow: 0 4px 16px rgba(57, 73, 171, 0.3);

    .balance-main {
      display: flex;
      align-items: baseline;
      gap: 16px;
      .balance-label {
        font-size: 16px;
        opacity: 0.9;
      }
      .balance-value {
        font-size: 36px;
        font-weight: bold;
      }
    }

    .points-alert {
      margin-top: 16px;
      background: rgba(255, 255, 255, 0.2);
      border: none;
      color: #fff;
      ::v-deep .el-alert__title {
        color: #fff;
      }
      ::v-deep .el-alert__icon {
        color: #ffc107;
      }
    }
  }

  .section-header {
    margin-bottom: 20px;
    .section-title {
      font-size: 20px;
      color: #333;
      margin: 0;
      display: flex;
      align-items: center;
      gap: 8px;
    }
  }

  .items-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
    gap: 24px;
  }

  .item-card {
    background: #fff;
    border-radius: 12px;
    overflow: hidden;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
    transition: all 0.3s ease;
    display: flex;
    flex-direction: column;

    &:hover {
      transform: translateY(-4px);
      box-shadow: 0 8px 24px rgba(57, 73, 171, 0.2);
    }

    .card-image {
      height: 140px;
      background: #e8eaf6;
      display: flex;
      align-items: center;
      justify-content: center;

      .image-placeholder {
        color: #9fa8da;
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 8px;
        i {
          font-size: 48px;
        }
      }
    }

    .card-info {
      padding: 16px;
      flex: 1;
      display: flex;
      flex-direction: column;

      .card-title {
        font-size: 16px;
        margin: 0 0 12px 0;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      .card-meta {
        display: flex;
        justify-content: space-between;
        margin-bottom: 12px;
        font-size: 14px;
        color: #666;
        .points-required {
          color: #f44336;
          font-weight: 500;
        }
      }

      .exchange-btn {
        width: 100%;
        margin-top: auto;
      }
    }
  }

  .empty-tip {
    grid-column: 1 / -1;
    text-align: center;
    padding: 60px 20px;
    color: #999;
    i {
      font-size: 48px;
      display: block;
      margin-bottom: 12px;
    }
  }

  .pagination-wrap {
    margin-top: 24px;
    padding: 16px 0;
    width: 100%;
    ::v-deep .pagination-container {
      background: transparent;
      padding: 0;
    }
  }
}
</style>
