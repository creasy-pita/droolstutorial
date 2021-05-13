package com.Runtime;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

public class GzGzyxs implements Serializable {
    /** 标识码 */
    private Long bsm;

    /** 运行时名称 */
    private String mc;

    /** 运行时描述 */
    private String ms;

    /** 运行时可用状态，0-禁用，1-可用 */
    private Integer kyzt;

    /** 是否为默认运行时，0-不是，1-是 */
    private Integer sfmryxs;

    /** 创建时间 */
    private Date cjsj;

    /** 更新时间 */
    private Date gxsj;

    /** 所属项目 */
    private Long ssxm;

    /** 运行时上下文 */
    private byte[] sxw;

    /** 规则信息标识码，逗号拼接 */
    private String gzxxbsm;

    /** 动态上下文，即上下文去除rule */
    private byte[] dtsxw;

    private static final long serialVersionUID = 1L;

    public GzGzyxs(Long bsm, String mc, String ms, Integer kyzt, Integer sfmryxs, Date cjsj, Date gxsj, Long ssxm) {
        this.bsm = bsm;
        this.mc = mc;
        this.ms = ms;
        this.kyzt = kyzt;
        this.sfmryxs = sfmryxs;
        this.cjsj = cjsj;
        this.gxsj = gxsj;
        this.ssxm = ssxm;
    }

    public GzGzyxs(Long bsm, String mc, String ms, Integer kyzt, Integer sfmryxs, Date cjsj, Date gxsj, Long ssxm, byte[] sxw, String gzxxbsm, byte[] dtsxw) {
        this.bsm = bsm;
        this.mc = mc;
        this.ms = ms;
        this.kyzt = kyzt;
        this.sfmryxs = sfmryxs;
        this.cjsj = cjsj;
        this.gxsj = gxsj;
        this.ssxm = ssxm;
        this.sxw = sxw;
        this.gzxxbsm = gzxxbsm;
        this.dtsxw = dtsxw;
    }

    public GzGzyxs() {
        super();
    }

    public Long getBsm() {
        return bsm;
    }

    public void setBsm(Long bsm) {
        this.bsm = bsm;
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc == null ? null : mc.trim();
    }

    public String getMs() {
        return ms;
    }

    public void setMs(String ms) {
        this.ms = ms == null ? null : ms.trim();
    }

    public Integer getKyzt() {
        return kyzt;
    }

    public void setKyzt(Integer kyzt) {
        this.kyzt = kyzt;
    }

    public Integer getSfmryxs() {
        return sfmryxs;
    }

    public void setSfmryxs(Integer sfmryxs) {
        this.sfmryxs = sfmryxs;
    }

    public Date getCjsj() {
        return cjsj;
    }

    public void setCjsj(Date cjsj) {
        this.cjsj = cjsj;
    }

    public Date getGxsj() {
        return gxsj;
    }

    public void setGxsj(Date gxsj) {
        this.gxsj = gxsj;
    }

    public Long getSsxm() {
        return ssxm;
    }

    public void setSsxm(Long ssxm) {
        this.ssxm = ssxm;
    }

    public byte[] getSxw() {
        return sxw;
    }

    public void setSxw(byte[] sxw) {
        this.sxw = sxw;
    }

    public String getGzxxbsm() {
        return gzxxbsm;
    }

    public void setGzxxbsm(String gzxxbsm) {
        this.gzxxbsm = gzxxbsm == null ? null : gzxxbsm.trim();
    }

    public byte[] getDtsxw() {
        return dtsxw;
    }

    public void setDtsxw(byte[] dtsxw) {
        this.dtsxw = dtsxw;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        GzGzyxs other = (GzGzyxs) that;
        return (this.getBsm() == null ? other.getBsm() == null : this.getBsm().equals(other.getBsm()))
            && (this.getMc() == null ? other.getMc() == null : this.getMc().equals(other.getMc()))
            && (this.getMs() == null ? other.getMs() == null : this.getMs().equals(other.getMs()))
            && (this.getKyzt() == null ? other.getKyzt() == null : this.getKyzt().equals(other.getKyzt()))
            && (this.getSfmryxs() == null ? other.getSfmryxs() == null : this.getSfmryxs().equals(other.getSfmryxs()))
            && (this.getCjsj() == null ? other.getCjsj() == null : this.getCjsj().equals(other.getCjsj()))
            && (this.getGxsj() == null ? other.getGxsj() == null : this.getGxsj().equals(other.getGxsj()))
            && (this.getSsxm() == null ? other.getSsxm() == null : this.getSsxm().equals(other.getSsxm()))
            && (Arrays.equals(this.getSxw(), other.getSxw()))
            && (this.getGzxxbsm() == null ? other.getGzxxbsm() == null : this.getGzxxbsm().equals(other.getGzxxbsm()))
            && (Arrays.equals(this.getDtsxw(), other.getDtsxw()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getBsm() == null) ? 0 : getBsm().hashCode());
        result = prime * result + ((getMc() == null) ? 0 : getMc().hashCode());
        result = prime * result + ((getMs() == null) ? 0 : getMs().hashCode());
        result = prime * result + ((getKyzt() == null) ? 0 : getKyzt().hashCode());
        result = prime * result + ((getSfmryxs() == null) ? 0 : getSfmryxs().hashCode());
        result = prime * result + ((getCjsj() == null) ? 0 : getCjsj().hashCode());
        result = prime * result + ((getGxsj() == null) ? 0 : getGxsj().hashCode());
        result = prime * result + ((getSsxm() == null) ? 0 : getSsxm().hashCode());
        result = prime * result + (Arrays.hashCode(getSxw()));
        result = prime * result + ((getGzxxbsm() == null) ? 0 : getGzxxbsm().hashCode());
        result = prime * result + (Arrays.hashCode(getDtsxw()));
        return result;
    }
}