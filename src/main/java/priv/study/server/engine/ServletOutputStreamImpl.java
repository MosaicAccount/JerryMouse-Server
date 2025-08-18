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

    private OutputStream outputStream;

    public ServletOutputStreamImpl(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {

    }

    @Override
    public void write(int b) throws IOException {

    }
}
