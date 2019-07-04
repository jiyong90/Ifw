<nav class="navbar navbar-expand-lg navbar-light ">
    <div class="container-fluid">
        <button type="button" id="sidebarCollapse" class="btn btn-info">
            <i class="fas fa-bars"></i>
            <!-- <span>Toggle Sidebar</span> -->
        </button>
        <button class="btn btn-dark d-inline-block d-lg-none ml-auto" type="button" data-toggle="collapse"
            data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false"
            aria-label="Toggle navigation">
            <i class="fas fa-align-justify"></i>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="nav navbar-nav ml-auto">
                <li class="nav-item active">
                    <a class="nav-link" href="#"><span class="icon-wrap mr-sm-2"><i class="fas fa-exchange-alt"></i></span><span>권한모드 전환</span></a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#"><span class="icon-wrap mr-sm-2"><i class="far fa-plus-square"></i></span><span>세션 늘리기</span></a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#"><span class="icon-wrap mr-sm-2"><i class="fas fa-cog"></i></span><span>설정</span></a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#" onClick="location.href='${rc.getContextPath()}/logout/${tsId}';"><span class="icon-wrap mr-sm-2"><i class="fas fa-power-off"></i></span><span>로그아웃</span></a>
                </li>
            </ul>
        </div>
    </div>
</nav>