<template>
  <div class="front-seat">
    <div v-loading="loading" class="seat-content">
      <div class="seat-header">
        <el-button icon="el-icon-arrow-left" size="small" @click="$router.back()">返回</el-button>
        <h2>选座购票</h2>
      </div>

      <div v-if="seats.length > 0" class="seat-area">
        <div
          v-for="area in seatMatrix"
          :key="area.name"
          class="area-block"
        >
          <div class="area-title">{{ area.name }}</div>
          <div class="seat-matrix" :style="matrixStyle(area)">
            <div
              v-for="seat in area.seats"
              :key="seat.seatId"
              :class="['seat-cell', seatClass(seat)]"
              @click="toggleSeat(seat)"
            >
              {{ seat.seatName || seat.seatId }}
            </div>
          </div>
        </div>
      </div>

      <div class="seat-legend">
        <span class="legend-item available"><i></i>可选</span>
        <span class="legend-item sold"><i></i>已售/锁定</span>
        <span class="legend-item selected"><i></i>已选</span>
      </div>

      <div v-if="selectedSeats.length > 0" class="order-bar">
        <div class="order-summary">
          <span>已选 {{ selectedSeats.length }} 座</span>
          <span class="total-price">¥{{ totalPrice }}</span>
        </div>
        <el-button type="primary" size="medium" :loading="submitting" @click="confirmOrder">
          确认下单
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { getSessionSeats, placeOrder } from '@/api/front/ticket'

export default {
  name: 'FrontSeat',
  data() {
    return {
      loading: false,
      submitting: false,
      sessionId: null,
      seats: [],
      selectedSeats: []
    }
  },
  computed: {
    seatMatrix() {
      const map = {}
      this.seats.forEach(s => {
        const name = s.areaName || '默认区'
        if (!map[name]) map[name] = { name, seats: [], maxCol: 0 }
        const { row, col } = this.parseSeatName(s.seatName)
        s._row = row
        s._col = col
        map[name].seats.push(s)
        map[name].maxCol = Math.max(map[name].maxCol, col)
      })
      return Object.values(map).map(area => {
        area.seats.sort((a, b) => (a._row - b._row) || (a._col - b._col))
        return area
      })
    },
    totalPrice() {
      return this.selectedSeats.reduce((sum, s) => sum + (parseFloat(s.price) || 0), 0).toFixed(2)
    }
  },
  created() {
    this.sessionId = this.$route.params.sessionId
    if (!this.$store.getters.token) {
      this.$message.warning('请先登录后选座')
      this.$router.replace('/login?redirect=' + encodeURIComponent(this.$route.fullPath))
      return
    }
    this.loadSeats()
  },
  methods: {
    loadSeats() {
      if (!this.sessionId) return
      this.loading = true
      getSessionSeats(this.sessionId).then(res => {
        this.seats = res.data || []
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    seatClass(seat) {
      if (this.selectedSeats.some(s => s.seatId === seat.seatId)) return 'selected'
      if (seat.status === '0') return 'available'
      return 'sold'
    },
    toggleSeat(seat) {
      if (seat.status !== '0') return
      const idx = this.selectedSeats.findIndex(s => s.seatId === seat.seatId)
      if (idx >= 0) {
        this.selectedSeats.splice(idx, 1)
      } else {
        this.selectedSeats.push(seat)
      }
    },
    parseSeatName(name) {
      if (!name) return { row: 0, col: 0 }
      const m = name.match(/(\d+)\s*排\s*(\d+)\s*座/) || name.match(/(\d+)[^\d]*(\d+)/)
      return m ? { row: parseInt(m[1], 10), col: parseInt(m[2], 10) } : { row: 0, col: 0 }
    },
    matrixStyle(area) {
      const cols = area.maxCol || 10
      return {
        gridTemplateColumns: `repeat(${cols}, 36px)`
      }
    },
    confirmOrder() {
      if (!this.$store.getters.token) {
        this.$message.warning('请先登录')
        this.$router.push('/login?redirect=' + encodeURIComponent(this.$route.fullPath))
        return
      }
      if (this.selectedSeats.length === 0) {
        this.$message.warning('请选择座位')
        return
      }
      this.submitting = true
      placeOrder(this.sessionId, this.selectedSeats.map(s => s.seatId)).then(res => {
        this.$message.success('下单成功，请尽快完成支付')
        this.submitting = false
        this.$router.push('/front')
      }).catch(() => {
        this.submitting = false
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.front-seat {
  .seat-content {
    background: #fff;
    border-radius: 12px;
    padding: 24px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  }

  .seat-header {
    display: flex;
    align-items: center;
    gap: 16px;
    margin-bottom: 24px;
    h2 { margin: 0; font-size: 20px; }
  }

  .seat-area {
    margin-bottom: 24px;
  }

  .area-block {
    margin-bottom: 24px;
    .area-title {
      font-size: 14px;
      font-weight: 500;
      margin-bottom: 12px;
      color: #333;
    }
  }

  .seat-matrix {
    display: grid;
    gap: 6px;
    justify-content: start;
  }

  .seat-cell {
    width: 36px;
    height: 36px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 12px;
    border-radius: 4px;
    cursor: pointer;
    transition: all 0.2s;
    &.available {
      background: #fff;
      border: 1px solid #e0e0e0;
      color: #333;
      &:hover { background: #e8f5e9; border-color: #4caf50; }
    }
    &.sold {
      background: #e0e0e0;
      color: #9e9e9e;
      cursor: not-allowed;
    }
    &.selected {
      background: #4caf50;
      border: 1px solid #388e3c;
      color: #fff;
    }
  }

  .seat-legend {
    display: flex;
    gap: 24px;
    margin-bottom: 24px;
    font-size: 13px;
    color: #666;
    .legend-item {
      display: flex;
      align-items: center;
      gap: 8px;
      i {
        width: 24px;
        height: 24px;
        border-radius: 4px;
      }
      &.available i { background: #fff; border: 1px solid #e0e0e0; }
      &.sold i { background: #e0e0e0; }
      &.selected i { background: #4caf50; }
    }
  }

  .order-bar {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 16px 20px;
    background: #f5f5f5;
    border-radius: 8px;
    .order-summary {
      .total-price {
        font-size: 20px;
        font-weight: bold;
        color: #f44336;
        margin-left: 12px;
      }
    }
  }
}
</style>
