package priv.study.server.engine;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author JLian
 * @version 0.0.1
 * @since 0.0.1
 */
public class ServletOutputStreamImpl extends ServletOutputStream {

    private final OutputStream outputStream;

    public ServletOutputStreamImpl(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * 用于检查输出流是否准备好接收数据
     *
     * @return
     */
    @Override
    public boolean isReady() {
        return true;
    }

    /**
     * 用于设置异步I/O的写入监听器
     *
     * @param writeListener the {@link WriteListener} that should be notified when it's possible to write
     *
     */
    @Override
    public void setWriteListener(WriteListener writeListener) {

    }

    @Override
    public void write(int b) throws IOException {
        this.outputStream.write(b);
    }

    @Override
    public void close() throws IOException {
        this.outputStream.close();
    }


}
